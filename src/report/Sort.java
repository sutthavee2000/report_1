package report;

import java.util.Comparator;



public class  Sort implements Comparator<custumer> {

    public int compare(custumer a, custumer b) {
        if (a.getDob().equals(b.getDob())) {
            return a.getName().compareTo(b.getName());
        } else {
            return a.getDob().compareTo(b.getDob());
        }

        //return a.name.compareTo(b.name);
    }

}