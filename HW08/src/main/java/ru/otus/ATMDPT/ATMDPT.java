package ru.otus.ATMDPT;

import java.util.ArrayList;
import java.util.List;

public class ATMDPT {
    private final List<ATM> atmdpts = new ArrayList<>();

    /**
     * register new observer.
     *
     * @param atm to register
     */
    public void register (ATM atm) {
        atmdpts.add(atm);
    }

    /**
     * unregister atm
     *
     * @param atm
     */
    
    public void unregister(ATM atm) {
        atmdpts.remove(atm);
    }

    /**
     * reset to begin position
     *
     */

    public void resetATM ()  {
        atmdpts.forEach(atm -> {
            try {
                    atm.onReset();
                } catch (AtmLoadException e) {
                    e.printStackTrace();
                }
        });
    }

    public int getTotal () {
        int result = 0;
        for (ATM atm : atmdpts) {
            result += atm.getTotal();
        }
        return result;
    }
}
