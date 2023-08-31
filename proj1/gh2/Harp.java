package gh2;

import deque.Deque;
import deque.LinkedListDeque;

public class Harp extends GuitarString {

    private static final double DECAY = .996 * 2; // energy decay factor
    private Deque<Double> buffer = new LinkedListDeque<>();
    public Harp(double frequency) {
        super(frequency);
    }

    @Override
     public void tic() {
        //       the average of the two multiplied by the DECAY factor.
        //       **Do not call StdAudio.play().**
        double frontSample = buffer.removeFirst();
        double secondSample = buffer.get(0);
        double addValue = DECAY * ((frontSample + secondSample) / 2);
        buffer.addLast(-addValue);
    }
}
