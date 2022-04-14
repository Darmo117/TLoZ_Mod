package net.darmo_creations.tloz_mod.entities;

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
    return this.direction;
  }
}
