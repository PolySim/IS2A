package game.composent;

import java.util.ArrayList;
import java.util.List;

public class Timer {
  private long startTime;
  private long elapsedTime;
  private boolean isRunning;
  private List<TimerListener> listeners;

  public interface TimerListener {
    void onTimeUpdate(long elapsedSeconds);
  }

  public Timer() {
    this.startTime = 0;
    this.elapsedTime = 0;
    this.isRunning = false;
    this.listeners = new ArrayList<>();
  }

  public void start() {
    if (!this.isRunning) {
      this.startTime = System.currentTimeMillis();
      this.isRunning = true;
    }
  }

  public long stop() {
    if (this.isRunning) {
      this.elapsedTime = (System.currentTimeMillis() - this.startTime) / 1000;
      this.isRunning = false;
    }
    return this.elapsedTime;
  }

  public long getElapsedSeconds() {
    if (this.isRunning) {
      return (System.currentTimeMillis() - this.startTime) / 1000;
    }
    return this.elapsedTime;
  }

  public void reset() {
    this.startTime = 0;
    this.elapsedTime = 0;
    this.isRunning = false;
  }

  public boolean isRunning() {
    return this.isRunning;
  }

  // Ajoute un listener pour être notifié des changements de temps
  public void addListener(TimerListener listener) {
    this.listeners.add(listener);
  }

  // Notifie tous les listeners du temps écoulé
  public void notifyListeners() {
    long elapsed = getElapsedSeconds();
    for (TimerListener listener : this.listeners) {
      listener.onTimeUpdate(elapsed);
    }
  }
}
