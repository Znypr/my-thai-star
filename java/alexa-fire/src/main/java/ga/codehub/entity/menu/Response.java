package ga.codehub.entity.menu;

import java.util.Arrays;

public class Response {
    public Content[] content;
    public Pageable pageable;
    public String totalElements;

    public String toStringNames() {
        StringBuilder sb = new StringBuilder();
        for (Content c : content) {
            sb.append(c.dish.name + ", ");
        }
        return sb.toString();
    }

    public String toStringDescription() {
        StringBuilder sb = new StringBuilder();
        for (Content c : content) {
            sb.append(c.dish.description);
        }
        return sb.toString();
    }

    public String toStringName() {
        StringBuilder sb = new StringBuilder();
        for (Content c : content) {
            sb.append(c.dish.name);
        }
        return sb.toString();
    }
}
