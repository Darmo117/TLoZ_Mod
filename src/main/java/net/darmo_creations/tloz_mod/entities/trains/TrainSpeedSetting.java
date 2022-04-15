package net.darmo_creations.tloz_mod.entities.trains;

public enum TrainSpeedSetting {
  REVERSE(-1),
  IDLE(0),
  FORWARD(1),
  FORWARD_FAST(2);

  private final int direction;

  TrainSpeedSetting(final int direction) {
    this.direction = direction;
  }

  public TrainSpeedSetting getPrevious() {
    switch (this) {
      case IDLE:
        return REVERSE;
      case FORWARD:
        return IDLE;
      case FORWARD_FAST:
        return FORWARD;
      default:
        return this;
    }
  }

  public TrainSpeedSetting getNext() {
    switch (this) {
      case REVERSE:
        return IDLE;
      case IDLE:
        return FORWARD;
      case FORWARD:
        return FORWARD_FAST;
      default:
        return this;
    }
  }

  public int getDirection() {
    return (int) Math.signum(this.direction);
  }

  public int getMagnitude() {
    return Math.abs(this.direction);
  }

  public int getID() {
    return this.ordinal();
  }

  public static TrainSpeedSetting fromID(final int id) {
    return values()[id % values().length];
  }
}
