package ga.codehub.entity.menu;

import java.util.Arrays;

public class Response {
    public Content[] content;
    public Pageable pageable;
    public String totalElements;

    public String toStringNames() {
        StringBuilder sb = new StringBuilder();
        /*for (Content c : content) {
            sb.append(c.dish.name + ", ");
        }*/
        for(int i = 0; i < content.length; i++){
            Content c = content[i];
            if(i == content.length-1){
                sb.append(c.dish.name);
            }else{
                sb.append(c.dish.name + ", ");
            }
        }
        return sb.toString();
    }

    public String toStringDescription() {
        StringBuilder sb = new StringBuilder();
        for (Content c : content) {
            sb.append(c.dish.description);
        }
        String descriptionDefault = sb.toString();
        String descriptionFinal = "";
        String[] parts = descriptionDefault.split(" ");
        for(int i = 0; i < parts.length; i++){
            String part = parts[i];
            if(part.equals("&")){
                part = "and";
            }
            if(i == 0){
                descriptionFinal = part;
            }else{
                descriptionFinal += " " + part;
            }
        }
        return descriptionFinal;
    }

    public String toStringName() {
        StringBuilder sb = new StringBuilder();
        for (Content c : content) {
            sb.append(c.dish.name);
        }
        return sb.toString();
    }
}
