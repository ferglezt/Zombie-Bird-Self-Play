package com.kilobolt.GameWorld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.kilobolt.GameObjects.Bird;
import com.kilobolt.GameObjects.ScrollHandler;
import com.kilobolt.ZBHelpers.AssetLoader;

public class GameWorld {

	private Bird bird;
	private ScrollHandler scroller;
	private Rectangle ground;
	private int score = 0;
    private int nextPipe = 1;

	private int midPointY;

	private GameState currentState;

	public enum GameState {
		READY, RUNNING, GAMEOVER
	}

	public GameWorld(int midPointY) {
		currentState = GameState.READY;
		this.midPointY = midPointY;
		bird = new Bird(33, midPointY - 5, 17, 12);
		// The grass should start 66 pixels below the midPointY
		scroller = new ScrollHandler(this, midPointY + 66);
		ground = new Rectangle(0, midPointY + 66, 137, 11);
	}

	public void update(float delta) {

		switch (currentState) {
			case READY:
				updateReady(delta);
				break;

			case RUNNING:
			default:
				updateRunning(delta);
				break;
		}

	}

	private void updateReady(float delta) {
		// Do nothing for now
	}

	public void updateRunning(float delta) {
		if (delta > .15f) {
			delta = .15f;
		}

		bird.update(delta);
		scroller.update(delta);

        int a = 17;
        int radiusScale = 10;

        boolean hasPassedP1 = Intersector.overlaps(
                new Circle(bird.getBoundingCircle().x, bird.getBoundingCircle().y, bird.getBoundingCircle().radius / radiusScale),
                new Rectangle().set(
                        scroller.getPipe1().getSkullUp().getX() + scroller.getPipe1().getSkullUp().getWidth() * (float)1.125,
                        scroller.getPipe1().getSkullUp().getY() + scroller.getPipe1().getSkullUp().getHeight(),
                        0,
                        100
                )
        );
        boolean hasPassedP2 = Intersector.overlaps(
                new Circle(bird.getBoundingCircle().x, bird.getBoundingCircle().y, bird.getBoundingCircle().radius / radiusScale),
                new Rectangle().set(
                        scroller.getPipe2().getSkullUp().getX() + scroller.getPipe2().getSkullUp().getWidth() * (float)1.125,
                        scroller.getPipe2().getSkullUp().getY() + scroller.getPipe2().getSkullUp().getHeight(),
                        0,
                        100
                )
        );
        boolean hasPassedP3 = Intersector.overlaps(
                new Circle(bird.getBoundingCircle().x, bird.getBoundingCircle().y, bird.getBoundingCircle().radius / radiusScale),
                new Rectangle().set(
                        scroller.getPipe3().getSkullUp().getX() + scroller.getPipe3().getSkullUp().getWidth() * (float)1.125,
                        scroller.getPipe3().getSkullUp().getY() + scroller.getPipe3().getSkullUp().getHeight(),
                        0,
                        100
                )
        );

        if(hasPassedP1) {
            nextPipe = 2;
        } else if(hasPassedP2) {
            nextPipe = 3;
        } else if(hasPassedP3) {
            nextPipe = 1;
        }

        if(!bird.isAlive()) {
            nextPipe = 1;
        }


        if((nextPipe == 1 && bird.getY() > scroller.getPipe1().getSkullDown().getY() - a) ||
            (nextPipe == 2 && bird.getY() > scroller.getPipe2().getSkullDown().getY() - a) ||
            (nextPipe == 3 && bird.getY() > scroller.getPipe3().getSkullDown().getY() - a)) {

            bird.onClick();
        }


		if (scroller.collides(bird) && bird.isAlive()) {
			scroller.stop();
			bird.die();
			AssetLoader.dead.play();
		}

		if (Intersector.overlaps(bird.getBoundingCircle(), ground)) {
			scroller.stop();
			bird.die();
			bird.decelerate();
			currentState = GameState.GAMEOVER;
		}
	}

	public Bird getBird() {
		return bird;

	}

	public ScrollHandler getScroller() {
		return scroller;
	}

	public int getScore() {
		return score;
	}

	public void addScore(int increment) {
		score += increment;
	}

	public boolean isReady() {
		return currentState == GameState.READY;
	}

	public void start() {
		currentState = GameState.RUNNING;
	}

	public void restart() {
		currentState = GameState.READY;
		score = 0;
		bird.onRestart(midPointY - 5);
		scroller.onRestart();
		currentState = GameState.READY;
	}

	public boolean isGameOver() {
		return currentState == GameState.GAMEOVER;
	}
}
