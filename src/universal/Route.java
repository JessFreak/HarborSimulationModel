package universal;

public class Route {
    private final Element element;
    private int priority = 0;
    private double probability = 1.0;

    public Route(Element element) {
        this.element = element;
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
}