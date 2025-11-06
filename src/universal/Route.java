package universal;

public class Route {
    private final Element element;
    private int priority = 0;
    private double probability = 1.0;
    private Block block = null;

    public Route(Element element) {
        this.element = element;
    }

    public boolean isBlocked(Job job) {
        if (block == null) {
            return false;
        }
        try {
            return block.call(job);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Element getElement() {
        return element;
    }

    public int getPriority() {
        return priority;
    }

    public double getProbability() {
        return probability;
    }

    @FunctionalInterface
    public interface Block {
        Boolean call(Job job);
    }
}