package com.team.mazerunner.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.team.mazerunner.entities.Player;
import com.team.mazerunner.world.LevelMap;

import java.util.List;

public class Enemy {

    private static final float DETECTION_RANGE = LevelMap.TILE_SIZE * 5f;
    private static final float FRONT_HALF_ANGLE = 90f;
    private static final float LOSE_TARGET_TIME = 2.5f;
    private static final float STEALTH_REACTION_TIME = 6f;
    private static final float STEALTH_KILL_RANGE = LevelMap.TILE_SIZE * 1.15f;
    private static final float TILE_CENTER_EPSILON = 2f;

    private final float startX;
    private final float startY;
    private final Rectangle bounds;
    private final List<Vector2> patrolPath;

    private float x;
    private float y;
    private int patrolIndex;
    private float timeSinceSeenPlayer;
    private float timePlayerBehind;
    private float lastKnownPlayerX;
    private float lastKnownPlayerY;
    private boolean vulnerableToStealthKill;
    private final Vector2 facingDirection = new Vector2(1f, 0f);

    private EnemyState state = EnemyState.PATROL;
    private MovementStrategy currentStrategy = new PatrolStrategy();

    public Enemy(float x, float y, List<Vector2> patrolPath) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.patrolPath = patrolPath;
        this.bounds = new Rectangle(x, y, 32, 32);
    }

    public void update(Player player, LevelMap levelMap, float delta) {
        if (state == EnemyState.DEAD) {
            return;
        }

        updateState(player, levelMap, delta);
        currentStrategy.move(this, player, levelMap, delta);
        bounds.setPosition(x, y);
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (state == EnemyState.DEAD) {
            return;
        }

        Color cloakColor;
        Color accentColor;

        if (state == EnemyState.CHASE || state == EnemyState.ALERT) {
            cloakColor = new Color(0.38f, 0.045f, 0.065f, 1f);
            accentColor = new Color(0.96f, 0.18f, 0.16f, 1f);
        } else if (state == EnemyState.RETURN) {
            cloakColor = new Color(0.36f, 0.24f, 0.10f, 1f);
            accentColor = new Color(0.90f, 0.54f, 0.18f, 1f);
        } else {
            cloakColor = new Color(0.16f, 0.08f, 0.18f, 1f);
            accentColor = new Color(0.60f, 0.24f, 0.55f, 1f);
        }

        renderShadow(shapeRenderer);
        renderBody(shapeRenderer, cloakColor, accentColor);
        renderLegs(shapeRenderer);
        renderMask(shapeRenderer, accentColor);
        renderArms(shapeRenderer, cloakColor, accentColor);
        renderFacingDirection(shapeRenderer, accentColor);
    }

    private void renderShadow(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.012f, 0.010f, 0.014f, 1f));
        shapeRenderer.rect(x + 3, y - 4, bounds.width - 6, 5);
        shapeRenderer.setColor(new Color(0.030f, 0.020f, 0.030f, 1f));
        shapeRenderer.rect(x + 7, y - 2, bounds.width - 14, 3);
    }

    private void renderBody(ShapeRenderer shapeRenderer, Color cloakColor, Color accentColor) {
        shapeRenderer.setColor(new Color(0.030f, 0.020f, 0.034f, 1f));
        shapeRenderer.rect(x + 6, y + 3, 20, 23);
        shapeRenderer.circle(x + 16, y + 25, 9);

        shapeRenderer.setColor(cloakColor);
        shapeRenderer.rect(x + 8, y + 5, 16, 18);
        shapeRenderer.circle(x + 16, y + 25, 7);

        shapeRenderer.setColor(new Color(0.045f, 0.025f, 0.055f, 1f));
        shapeRenderer.rect(x + 10, y + 6, 12, 17);
        shapeRenderer.setColor(accentColor);
        shapeRenderer.rect(x + 10, y + 8, 12, 2);
        shapeRenderer.rect(x + 15, y + 8, 2, 11);

        shapeRenderer.setColor(new Color(0.12f, 0.08f, 0.13f, 1f));
        shapeRenderer.rect(x + 7, y + 17, 18, 4);
        shapeRenderer.setColor(accentColor);
        shapeRenderer.rect(x + 9, y + 18, 4, 2);
        shapeRenderer.rect(x + 19, y + 18, 4, 2);
    }

    private void renderLegs(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(new Color(0.045f, 0.025f, 0.050f, 1f));

        if (Math.abs(facingDirection.x) > Math.abs(facingDirection.y)) {
            if (facingDirection.x > 0f) {
                shapeRenderer.rect(x + 9, y + 1, 5, 7);
                shapeRenderer.rect(x + 20, y, 5, 7);
            } else {
                shapeRenderer.rect(x + 7, y, 5, 7);
                shapeRenderer.rect(x + 18, y + 1, 5, 7);
            }
        } else {
            shapeRenderer.rect(x + 8, y, 5, 7);
            shapeRenderer.rect(x + 19, y, 5, 7);
        }

        shapeRenderer.setColor(new Color(0.10f, 0.045f, 0.10f, 1f));
        shapeRenderer.rect(x + 7, y, 7, 2);
        shapeRenderer.rect(x + 18, y, 7, 2);
    }

    private void renderArms(ShapeRenderer shapeRenderer, Color cloakColor, Color accentColor) {
        shapeRenderer.setColor(new Color(0.040f, 0.020f, 0.050f, 1f));

        if (Math.abs(facingDirection.x) > Math.abs(facingDirection.y)) {
            if (facingDirection.x > 0f) {
                shapeRenderer.rect(x + 23, y + 10, 4, 11);
                shapeRenderer.rect(x + 5, y + 9, 4, 9);
                shapeRenderer.setColor(accentColor);
                shapeRenderer.rect(x + 26, y + 13, 3, 5);
            } else {
                shapeRenderer.rect(x + 5, y + 10, 4, 11);
                shapeRenderer.rect(x + 23, y + 9, 4, 9);
                shapeRenderer.setColor(accentColor);
                shapeRenderer.rect(x + 3, y + 13, 3, 5);
            }
        } else {
            shapeRenderer.rect(x + 5, y + 9, 4, 10);
            shapeRenderer.rect(x + 23, y + 9, 4, 10);
            shapeRenderer.setColor(accentColor);
            shapeRenderer.rect(x + 6, y + 15, 3, 3);
            shapeRenderer.rect(x + 23, y + 15, 3, 3);
        }
    }

    public void moveToward(float targetX, float targetY, float speed, LevelMap levelMap, float delta) {
        Vector2 nextStep = levelMap.findNextStep(x, y, targetX, targetY);

        if (nextStep == null) {
            setState(EnemyState.RETURN);
            return;
        }

        float stepDistance = speed * delta;
        float currentTileCenterX = levelMap.getTileCenterX(x + bounds.width / 2f);
        float currentTileCenterY = levelMap.getTileCenterY(y + bounds.height / 2f);
        float diffX = nextStep.x - x;
        float diffY = nextStep.y - y;

        if (Math.abs(diffX) <= TILE_CENTER_EPSILON && Math.abs(diffY) <= TILE_CENTER_EPSILON) {
            x = nextStep.x;
            y = nextStep.y;
            return;
        }

        if (Math.abs(diffX) > TILE_CENTER_EPSILON) {
            if (Math.abs(y - currentTileCenterY) > TILE_CENTER_EPSILON) {
                moveOnYAxis(currentTileCenterY - y, stepDistance, levelMap);
                return;
            }

            moveOnXAxis(diffX, stepDistance, levelMap);
            return;
        }

        if (Math.abs(diffY) > TILE_CENTER_EPSILON) {
            if (Math.abs(x - currentTileCenterX) > TILE_CENTER_EPSILON) {
                moveOnXAxis(currentTileCenterX - x, stepDistance, levelMap);
                return;
            }

            moveOnYAxis(diffY, stepDistance, levelMap);
        }
    }

    private void moveOnXAxis(float diffX, float stepDistance, LevelMap levelMap) {
        float movement = Math.signum(diffX) * Math.min(Math.abs(diffX), stepDistance);
        float nextX = x + movement;

        facingDirection.set(Math.signum(diffX), 0f);

        if (!levelMap.isBlocked(nextX, y, (int) bounds.width, (int) bounds.height)) {
            x = nextX;
        }
    }

    private void moveOnYAxis(float diffY, float stepDistance, LevelMap levelMap) {
        float movement = Math.signum(diffY) * Math.min(Math.abs(diffY), stepDistance);
        float nextY = y + movement;

        facingDirection.set(0f, Math.signum(diffY));

        if (!levelMap.isBlocked(x, nextY, (int) bounds.width, (int) bounds.height)) {
            y = nextY;
        }
    }

    public boolean overlaps(Player player) {
        return bounds.overlaps(player.getBounds());
    }

    public boolean isNear(Player player, float distance) {
        return Vector2.dst(x, y, player.getX(), player.getY()) <= distance;
    }

    public float distanceTo(Player player) {
        return Vector2.dst(x, y, player.getX(), player.getY());
    }

    public boolean canBeStealthKilledBy(Player player, LevelMap levelMap) {
        return isInStealthKillPosition(player, levelMap) &&
                state != EnemyState.CHASE &&
                state != EnemyState.ALERT &&
                timePlayerBehind < STEALTH_REACTION_TIME;
    }

    public boolean canCatch(Player player, LevelMap levelMap) {
        if (!overlaps(player)) {
            return false;
        }

        if (state == EnemyState.CHASE || state == EnemyState.ALERT) {
            return true;
        }

        return isPlayerInFront(player, levelMap);
    }

    public boolean isInStealthKillPosition(Player player, LevelMap levelMap) {
        Vector2 toPlayer = new Vector2(player.getX() - x, player.getY() - y);

        if (toPlayer.len() > STEALTH_KILL_RANGE || toPlayer.isZero(1f)) {
            return false;
        }

        if (facingDirection.angleDeg(toPlayer) <= FRONT_HALF_ANGLE) {
            return false;
        }

        return levelMap.hasLineOfSight(
                x + bounds.width / 2f,
                y + bounds.height / 2f,
                player.getX() + player.getWidth() / 2f,
                player.getY() + player.getHeight() / 2f
        );
    }

    private boolean isPlayerInFront(Player player, LevelMap levelMap) {
        Vector2 toPlayer = new Vector2(player.getX() - x, player.getY() - y);

        if (toPlayer.isZero(1f) || facingDirection.angleDeg(toPlayer) > FRONT_HALF_ANGLE) {
            return false;
        }

        return levelMap.hasLineOfSight(
                x + bounds.width / 2f,
                y + bounds.height / 2f,
                player.getX() + player.getWidth() / 2f,
                player.getY() + player.getHeight() / 2f
        );
    }

    public void kill() {
        setState(EnemyState.DEAD);
    }

    public void resetToPatrol() {
        x = startX;
        y = startY;
        bounds.setPosition(x, y);
        patrolIndex = 0;
        timeSinceSeenPlayer = 0f;
        timePlayerBehind = 0f;
        vulnerableToStealthKill = false;
        facingDirection.set(1f, 0f);
        setState(EnemyState.PATROL);
    }

    public Vector2 getCurrentPatrolTarget() {
        return patrolPath.get(patrolIndex);
    }

    public void advancePatrolTarget() {
        patrolIndex = (patrolIndex + 1) % patrolPath.size();
    }

    public boolean isCloseTo(float targetX, float targetY, float distance) {
        return Vector2.dst(x, y, targetX, targetY) <= distance;
    }

    public void setState(EnemyState nextState) {
        if (state == nextState) {
            return;
        }

        state = nextState;

        if (state == EnemyState.PATROL) {
            currentStrategy = new PatrolStrategy();
        } else if (state == EnemyState.CHASE || state == EnemyState.ALERT) {
            currentStrategy = new ChaseStrategy();
        } else if (state == EnemyState.RETURN) {
            currentStrategy = new ReturnStrategy();
        }
    }

    public float getPatrolSpeed() {
        return 90f;
    }

    public float getChaseSpeed() {
        return 145f;
    }

    public float getStartX() {
        return startX;
    }

    public float getStartY() {
        return startY;
    }

    public float getLastKnownPlayerX() {
        return lastKnownPlayerX;
    }

    public float getLastKnownPlayerY() {
        return lastKnownPlayerY;
    }

    private void updateState(Player player, LevelMap levelMap, float delta) {
        vulnerableToStealthKill = false;

        if (canSeeInCurrentDirection(player, levelMap)) {
            timeSinceSeenPlayer = 0f;
            timePlayerBehind = 0f;
            lastKnownPlayerX = player.getX();
            lastKnownPlayerY = player.getY();
            setState(EnemyState.CHASE);
            return;
        }

        if (state == EnemyState.CHASE) {
            timeSinceSeenPlayer += delta;

            if (timeSinceSeenPlayer >= LOSE_TARGET_TIME) {
                timePlayerBehind = 0f;
                setState(EnemyState.RETURN);
            }

            return;
        }

        if (canSensePlayerBehind(player, levelMap)) {
            timePlayerBehind += delta;
            vulnerableToStealthKill = timePlayerBehind < STEALTH_REACTION_TIME;

            if (timePlayerBehind >= STEALTH_REACTION_TIME) {
                Vector2 toPlayer = new Vector2(player.getX() - x, player.getY() - y);
                facingDirection.set(toPlayer).nor();
                lastKnownPlayerX = player.getX();
                lastKnownPlayerY = player.getY();
                setState(EnemyState.CHASE);
            }

            return;
        }

        timePlayerBehind = 0f;
    }

    private boolean canSeeInCurrentDirection(Player player, LevelMap levelMap) {
        Vector2 toPlayer = new Vector2(player.getX() - x, player.getY() - y);

        if (toPlayer.len() > DETECTION_RANGE || toPlayer.isZero(1f)) {
            return false;
        }

        boolean hasLineOfSight = levelMap.hasLineOfSight(
                x + bounds.width / 2f,
                y + bounds.height / 2f,
                player.getX() + player.getWidth() / 2f,
                player.getY() + player.getHeight() / 2f
        );

        if (!hasLineOfSight) {
            return false;
        }

        if (state != EnemyState.CHASE && facingDirection.angleDeg(toPlayer) > FRONT_HALF_ANGLE) {
            return false;
        }

        facingDirection.set(toPlayer).nor();
        return true;
    }

    private boolean canSensePlayerBehind(Player player, LevelMap levelMap) {
        Vector2 toPlayer = new Vector2(player.getX() - x, player.getY() - y);

        if (toPlayer.len() > DETECTION_RANGE || toPlayer.isZero(1f)) {
            return false;
        }

        if (facingDirection.angleDeg(toPlayer) <= FRONT_HALF_ANGLE) {
            return false;
        }

        return levelMap.hasLineOfSight(
                x + bounds.width / 2f,
                y + bounds.height / 2f,
                player.getX() + player.getWidth() / 2f,
                player.getY() + player.getHeight() / 2f
        );
    }

    private void renderFacingDirection(ShapeRenderer shapeRenderer, Color accentColor) {
        shapeRenderer.setColor(new Color(accentColor.r, accentColor.g, accentColor.b, 1f));

        if (Math.abs(facingDirection.x) > Math.abs(facingDirection.y)) {
            if (facingDirection.x > 0f) {
                shapeRenderer.rect(x + 23, y + 24, 6, 2);
                shapeRenderer.rect(x + 28, y + 22, 2, 6);
            } else {
                shapeRenderer.rect(x + 3, y + 24, 6, 2);
                shapeRenderer.rect(x + 2, y + 22, 2, 6);
            }
        } else if (facingDirection.y > 0f) {
            shapeRenderer.rect(x + 12, y + 29, 8, 3);
            shapeRenderer.rect(x + 15, y + 32, 2, 3);
        } else {
            shapeRenderer.rect(x + 12, y + 20, 8, 2);
        }
    }

    private void renderMask(ShapeRenderer shapeRenderer, Color accentColor) {
        shapeRenderer.setColor(new Color(0.055f, 0.025f, 0.045f, 1f));

        if (facingDirection.y > 0.45f) {
            shapeRenderer.rect(x + 9, y + 22, 14, 6);
            shapeRenderer.setColor(accentColor);
            shapeRenderer.rect(x + 12, y + 26, 8, 2);
            shapeRenderer.setColor(new Color(0.020f, 0.012f, 0.018f, 1f));
            shapeRenderer.rect(x + 11, y + 23, 10, 2);
            return;
        }

        if (facingDirection.y < -0.45f) {
            shapeRenderer.rect(x + 9, y + 21, 14, 6);
            shapeRenderer.setColor(accentColor);
            shapeRenderer.rect(x + 12, y + 23, 3, 3);
            shapeRenderer.rect(x + 18, y + 23, 3, 3);
            shapeRenderer.setColor(new Color(0.95f, 0.78f, 0.72f, 1f));
            shapeRenderer.rect(x + 13, y + 24, 1, 1);
            shapeRenderer.rect(x + 19, y + 24, 1, 1);
            return;
        }

        shapeRenderer.rect(x + 10, y + 21, 12, 6);
        shapeRenderer.setColor(accentColor);
        if (facingDirection.x >= 0f) {
            shapeRenderer.rect(x + 18, y + 23, 4, 3);
            shapeRenderer.setColor(new Color(0.95f, 0.78f, 0.72f, 1f));
            shapeRenderer.rect(x + 20, y + 24, 1, 1);
        } else {
            shapeRenderer.rect(x + 10, y + 23, 4, 3);
            shapeRenderer.setColor(new Color(0.95f, 0.78f, 0.72f, 1f));
            shapeRenderer.rect(x + 11, y + 24, 1, 1);
        }
    }
}
