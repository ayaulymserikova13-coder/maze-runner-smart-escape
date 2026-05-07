package com.team.mazerunner.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.team.mazerunner.entities.Player;
import com.team.mazerunner.world.LevelMap;

import java.util.List;
import java.util.Random;

public class Enemy {

    private static final float FLASHLIGHT_RANGE = LevelMap.TILE_SIZE * 4f;
    private static final float FLASHLIGHT_ANGLE = 38f;
    private static final float BACK_BLIND_ANGLE = 38f;
    private static final float LOSE_TARGET_TIME = 2.5f;
    private static final float STEALTH_REACTION_TIME = 6f;
    private static final float STEALTH_KILL_RANGE = LevelMap.TILE_SIZE * 1.15f;
    private static final float TILE_CENTER_EPSILON = 2f;
    private static final float MIN_PATROL_PAUSE = 0.15f;
    private static final float MAX_PATROL_PAUSE = 0.75f;
    private static final float TURN_SPEED_DEGREES = 360f;
    private static final float TURN_EPSILON_DEGREES = 3f;
    private static final int FLASHLIGHT_RAYS = 28;

    private final float startX;
    private final float startY;
    private final Rectangle bounds;
    private final List<Vector2> patrolPath;
    private final Random random;
    private final float patrolSpeed;
    private final float chaseSpeed;

    private float x;
    private float y;
    private int patrolIndex;
    private int patrolDirection = 1;
    private float patrolPauseTimer;
    private float timeSinceSeenPlayer;
    private float timePlayerBehind;
    private float lastKnownPlayerX;
    private float lastKnownPlayerY;
    private float currentAngle;
    private float targetAngle;
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
        this.random = new Random(Float.floatToIntBits(x * 31f + y * 17f));
        this.patrolSpeed = 78f + random.nextFloat() * 28f;
        this.chaseSpeed = 140f + random.nextFloat() * 18f;

        if (patrolPath.size() > 1) {
            this.patrolIndex = random.nextInt(patrolPath.size());
            this.patrolDirection = random.nextBoolean() ? 1 : -1;
            this.patrolPauseTimer = random.nextFloat() * MAX_PATROL_PAUSE;
        }
    }

    public void update(Player player, LevelMap levelMap, float delta) {
        if (state == EnemyState.DEAD) {
            return;
        }

        updateState(player, levelMap, delta);
        currentStrategy.move(this, player, levelMap, delta);
        updateTurn(delta);
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

    public void renderFlashlight(ShapeRenderer shapeRenderer, LevelMap levelMap) {
        if (state == EnemyState.DEAD) {
            return;
        }

        Color outerColor;
        Color midColor;
        Color innerColor;

        if (state == EnemyState.CHASE || state == EnemyState.ALERT) {
            outerColor = new Color(0.40f, 0.08f, 0.07f, 0.035f);
            midColor = new Color(0.48f, 0.12f, 0.09f, 0.055f);
            innerColor = new Color(0.58f, 0.18f, 0.12f, 0.075f);
        } else if (state == EnemyState.RETURN) {
            outerColor = new Color(0.23f, 0.20f, 0.13f, 0.030f);
            midColor = new Color(0.32f, 0.27f, 0.16f, 0.045f);
            innerColor = new Color(0.42f, 0.34f, 0.18f, 0.060f);
        } else {
            outerColor = new Color(0.14f, 0.20f, 0.18f, 0.030f);
            midColor = new Color(0.22f, 0.30f, 0.24f, 0.045f);
            innerColor = new Color(0.34f, 0.42f, 0.30f, 0.062f);
        }

        shapeRenderer.setColor(outerColor);
        renderFlashlightFan(shapeRenderer, levelMap, FLASHLIGHT_RANGE, FLASHLIGHT_ANGLE);

        shapeRenderer.setColor(midColor);
        renderFlashlightFan(shapeRenderer, levelMap, FLASHLIGHT_RANGE * 0.78f, FLASHLIGHT_ANGLE * 0.72f);

        shapeRenderer.setColor(innerColor);
        renderFlashlightFan(shapeRenderer, levelMap, FLASHLIGHT_RANGE * 0.48f, FLASHLIGHT_ANGLE * 0.38f);

        renderFlashlightGlow(shapeRenderer, innerColor);
    }

    private void renderFlashlightFan(ShapeRenderer shapeRenderer, LevelMap levelMap, float range, float halfAngle) {
        float originX = getCenterX();
        float originY = getCenterY();
        Vector2 previousPoint = getFlashlightRayEnd(levelMap, -halfAngle, range);

        for (int i = 1; i <= FLASHLIGHT_RAYS; i++) {
            float angleOffset = -halfAngle + (halfAngle * 2f * i / FLASHLIGHT_RAYS);
            Vector2 currentPoint = getFlashlightRayEnd(levelMap, angleOffset, range);
            shapeRenderer.triangle(originX, originY, previousPoint.x, previousPoint.y, currentPoint.x, currentPoint.y);
            previousPoint = currentPoint;
        }
    }

    private void renderFlashlightGlow(ShapeRenderer shapeRenderer, Color color) {
        Vector2 visualDirection = getVisualDirection();
        float glowX = getCenterX() + visualDirection.x * 12f;
        float glowY = getCenterY() + visualDirection.y * 12f;

        shapeRenderer.setColor(new Color(color.r, color.g, color.b, color.a * 0.65f));
        shapeRenderer.circle(glowX, glowY, 9f);
        shapeRenderer.setColor(new Color(color.r, color.g, color.b, color.a * 0.35f));
        shapeRenderer.circle(glowX, glowY, 16f);
    }

    private Vector2 getFlashlightRayEnd(LevelMap levelMap, float angleOffset, float range) {
        float originX = getCenterX();
        float originY = getCenterY();
        Vector2 ray = getVisualDirection();

        if (ray.isZero(0.001f)) {
            ray.set(1f, 0f);
        }

        ray.nor().rotateDeg(angleOffset);
        Vector2 lastVisiblePoint = new Vector2(originX, originY);

        for (float distance = LevelMap.TILE_SIZE / 4f; distance <= range; distance += LevelMap.TILE_SIZE / 4f) {
            float checkX = originX + ray.x * distance;
            float checkY = originY + ray.y * distance;

            if (!levelMap.hasLineOfSight(originX, originY, checkX, checkY)) {
                break;
            }

            lastVisiblePoint.set(checkX, checkY);
        }

        return lastVisiblePoint;
    }

    private Vector2 getVisualDirection() {
        return new Vector2(1f, 0f).rotateDeg(currentAngle).nor();
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
        Vector2 visualDirection = getVisualDirection();

        if (Math.abs(visualDirection.x) > Math.abs(visualDirection.y)) {
            if (visualDirection.x > 0f) {
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
        Vector2 visualDirection = getVisualDirection();

        if (Math.abs(visualDirection.x) > Math.abs(visualDirection.y)) {
            if (visualDirection.x > 0f) {
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
        setTargetAngleFromDirection(facingDirection);

        if (isTurning()) {
            return;
        }

        if (!levelMap.isBlocked(nextX, y, (int) bounds.width, (int) bounds.height)) {
            x = nextX;
        }
    }

    private void moveOnYAxis(float diffY, float stepDistance, LevelMap levelMap) {
        float movement = Math.signum(diffY) * Math.min(Math.abs(diffY), stepDistance);
        float nextY = y + movement;

        facingDirection.set(0f, Math.signum(diffY));
        setTargetAngleFromDirection(facingDirection);

        if (isTurning()) {
            return;
        }

        if (!levelMap.isBlocked(x, nextY, (int) bounds.width, (int) bounds.height)) {
            y = nextY;
        }
    }

    private void setTargetAngleFromDirection(Vector2 direction) {
        if (direction.isZero(0.001f)) {
            return;
        }

        targetAngle = normalizeAngle(direction.angleDeg());
    }

    private void updateTurn(float delta) {
        float difference = signedAngleDifference(currentAngle, targetAngle);
        float step = TURN_SPEED_DEGREES * delta;

        if (Math.abs(difference) <= Math.min(step, TURN_EPSILON_DEGREES)) {
            currentAngle = targetAngle;
            return;
        }

        currentAngle = normalizeAngle(currentAngle + Math.signum(difference) * step);
    }

    private boolean isTurning() {
        return Math.abs(signedAngleDifference(currentAngle, targetAngle)) > TURN_EPSILON_DEGREES;
    }

    private float signedAngleDifference(float fromAngle, float toAngle) {
        float difference = normalizeAngle(toAngle - fromAngle);

        if (difference > 180f) {
            difference -= 360f;
        }

        if (Math.abs(Math.abs(difference) - 180f) < 0.001f) {
            return -180f;
        }

        return difference;
    }

    private float normalizeAngle(float angle) {
        float normalized = angle % 360f;
        return normalized < 0f ? normalized + 360f : normalized;
    }

    public boolean overlaps(Player player) {
        if (state == EnemyState.DEAD) {
            return false;
        }

        return bounds.overlaps(player.getBounds());
    }

    public boolean isNear(Player player, float distance) {
        return Vector2.dst(x, y, player.getX(), player.getY()) <= distance;
    }

    public float distanceTo(Player player) {
        return Vector2.dst(x, y, player.getX(), player.getY());
    }

    private float getCenterX() {
        return x + bounds.width / 2f;
    }

    private float getCenterY() {
        return y + bounds.height / 2f;
    }

    private float getPlayerCenterX(Player player) {
        return player.getX() + player.getWidth() / 2f;
    }

    private float getPlayerCenterY(Player player) {
        return player.getY() + player.getHeight() / 2f;
    }

    private Vector2 vectorToPlayer(Player player) {
        return new Vector2(getPlayerCenterX(player) - getCenterX(), getPlayerCenterY(player) - getCenterY());
    }

    private boolean hasLineOfSightToPlayer(Player player, LevelMap levelMap) {
        return levelMap.hasLineOfSight(
                getCenterX(),
                getCenterY(),
                getPlayerCenterX(player),
                getPlayerCenterY(player)
        );
    }

    private boolean isBehindEnemy(Vector2 toPlayer) {
        return facingDirection.angleDeg(toPlayer) >= 180f - BACK_BLIND_ANGLE;
    }

    public boolean canBeStealthKilledBy(Player player, LevelMap levelMap) {
        if (state == EnemyState.DEAD) {
            return false;
        }

        return isInStealthKillPosition(player, levelMap) &&
                state != EnemyState.CHASE &&
                state != EnemyState.ALERT &&
                timePlayerBehind < STEALTH_REACTION_TIME;
    }

    public boolean canCatch(Player player, LevelMap levelMap) {
        if (state == EnemyState.DEAD) {
            return false;
        }

        if (!overlaps(player)) {
            return false;
        }

        if (state == EnemyState.CHASE || state == EnemyState.ALERT) {
            return true;
        }

        return isPlayerInFront(player, levelMap);
    }

    public boolean isInStealthKillPosition(Player player, LevelMap levelMap) {
        Vector2 toPlayer = vectorToPlayer(player);

        if (toPlayer.len() > STEALTH_KILL_RANGE || toPlayer.isZero(1f)) {
            return false;
        }

        if (!isBehindEnemy(toPlayer)) {
            return false;
        }

        return hasLineOfSightToPlayer(player, levelMap);
    }

    private boolean isPlayerInFront(Player player, LevelMap levelMap) {
        Vector2 toPlayer = vectorToPlayer(player);

        if (toPlayer.isZero(1f) || facingDirection.angleDeg(toPlayer) > FLASHLIGHT_ANGLE) {
            return false;
        }

        return hasLineOfSightToPlayer(player, levelMap);
    }

    public void kill() {
        setState(EnemyState.DEAD);
    }

    public void resetToPatrol() {
        if (state == EnemyState.DEAD) {
            return;
        }

        x = startX;
        y = startY;
        bounds.setPosition(x, y);
        patrolIndex = patrolPath.size() > 1 ? random.nextInt(patrolPath.size()) : 0;
        patrolDirection = random.nextBoolean() ? 1 : -1;
        patrolPauseTimer = random.nextFloat() * MAX_PATROL_PAUSE;
        timeSinceSeenPlayer = 0f;
        timePlayerBehind = 0f;
        vulnerableToStealthKill = false;
        facingDirection.set(1f, 0f);
        setState(EnemyState.PATROL);
    }

    public Vector2 getCurrentPatrolTarget() {
        return patrolPath.get(patrolIndex);
    }

    public boolean isPatrolPaused(float delta) {
        if (patrolPauseTimer <= 0f) {
            return false;
        }

        patrolPauseTimer = Math.max(0f, patrolPauseTimer - delta);
        return true;
    }

    public void advancePatrolTarget() {
        if (patrolPath.size() <= 1) {
            return;
        }

        patrolPauseTimer = MIN_PATROL_PAUSE + random.nextFloat() * (MAX_PATROL_PAUSE - MIN_PATROL_PAUSE);

        if (random.nextFloat() < 0.35f) {
            patrolDirection *= -1;
        }

        patrolIndex = (patrolIndex + patrolDirection + patrolPath.size()) % patrolPath.size();
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
        return patrolSpeed;
    }

    public float getChaseSpeed() {
        return chaseSpeed;
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

        if (canDetectPlayer(player, levelMap)) {
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

        timePlayerBehind = 0f;
    }

    private boolean canDetectPlayer(Player player, LevelMap levelMap) {
        Vector2 toPlayer = vectorToPlayer(player);

        if (toPlayer.isZero(1f) || !hasLineOfSightToPlayer(player, levelMap)) {
            return false;
        }

        return isInsideFlashlight(toPlayer);
    }

    private boolean isInsideFlashlight(Vector2 toPlayer) {
        return toPlayer.len() <= FLASHLIGHT_RANGE && facingDirection.angleDeg(toPlayer) <= FLASHLIGHT_ANGLE;
    }

    private void renderFacingDirection(ShapeRenderer shapeRenderer, Color accentColor) {
        shapeRenderer.setColor(new Color(accentColor.r, accentColor.g, accentColor.b, 1f));
        Vector2 visualDirection = getVisualDirection();

        if (Math.abs(visualDirection.x) > Math.abs(visualDirection.y)) {
            if (visualDirection.x > 0f) {
                shapeRenderer.rect(x + 23, y + 24, 6, 2);
                shapeRenderer.rect(x + 28, y + 22, 2, 6);
            } else {
                shapeRenderer.rect(x + 3, y + 24, 6, 2);
                shapeRenderer.rect(x + 2, y + 22, 2, 6);
            }
        } else if (visualDirection.y > 0f) {
            shapeRenderer.rect(x + 12, y + 29, 8, 3);
            shapeRenderer.rect(x + 15, y + 32, 2, 3);
        } else {
            shapeRenderer.rect(x + 12, y + 20, 8, 2);
        }
    }

    private void renderMask(ShapeRenderer shapeRenderer, Color accentColor) {
        shapeRenderer.setColor(new Color(0.055f, 0.025f, 0.045f, 1f));
        Vector2 visualDirection = getVisualDirection();

        if (visualDirection.y > 0.45f) {
            shapeRenderer.rect(x + 9, y + 22, 14, 6);
            shapeRenderer.setColor(accentColor);
            shapeRenderer.rect(x + 12, y + 26, 8, 2);
            shapeRenderer.setColor(new Color(0.020f, 0.012f, 0.018f, 1f));
            shapeRenderer.rect(x + 11, y + 23, 10, 2);
            return;
        }

        if (visualDirection.y < -0.45f) {
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
        if (visualDirection.x >= 0f) {
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
