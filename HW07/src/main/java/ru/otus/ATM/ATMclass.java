package ru.otus.ATM;


import java.util.*;

public class ATMclass {
    private List<cashbox> cashboxes = new ArrayList<>();

    public int getTotal() {
        int total = 0;
        for (cashbox cb : cashboxes) {
            total += cb.getTotal();
        }
        return total;
    }

    public String getStringTotal() {
        return String.valueOf(getTotal());
    }
    public String getStatus() {
        String total = "";
        for (cashbox cb : cashboxes) {
            total += "купюра "+cb.getValue()+":"+cb.getCount()+";";
        }
        return total;
    }
    private List<cashbox> trywithdraw(int total) {
        List<cashbox> withdraws = new ArrayList<>();
        for (cashbox cb : cashboxes) {
            if (total>0) {
                int countvalue = (total / cb.getValue());
                if (countvalue>0) {
                    if (countvalue>cb.getCount()) {
                        countvalue = cb.getCount();
                    }
                    withdraws.add(new cashbox(cb.getValue(),countvalue));
                    total -= countvalue*cb.getValue();
                }
            }
        }
        if (total>0){
            withdraws.clear();
        }
        return withdraws;
    }

    private String withdrawString (List<cashbox> withdraws) {
        String stringwithdraw = "";
        for (cashbox cb : withdraws) {
            stringwithdraw += "купюра "+cb.getValue()+":"+cb.getCount()+";";
        }
        return stringwithdraw;
    }

    private void withdrawCommit (List<cashbox> withdraws) {
        for (cashbox cb : cashboxes) {
            for (cashbox cw : withdraws) {
                if (cw.equals(cb)) {
                    cb.decCount(cw.getCount());
                    break;
                }
            }
        }
    }

    public String withdraw(int total) {
        if (total>0) {
            List<cashbox> withdraws = trywithdraw(total);

            if (withdraws.size()>0) {
                withdrawCommit (withdraws);//уменьшить количество купюр в банкомате
                return "выдача успешна "+withdrawString(withdraws);
            } else {
                return "выдача суммы невозможна";
            }
        } else {
            return "суммы не положительная";
        }


    }

    public void addCashbox (int value, int count) {
        for (cashbox cb : cashboxes) {
            if (cb.getValue()==value) {
               cb.addCount(count);
               count = 0;
               break;
            };
        }
        if (count>0) {
            cashboxes.add(new cashbox(value,count));
            Collections.sort(cashboxes,Comparator.comparing(cashbox::getValue).reversed());//сразу пачки денег сортируем от большей к меньшей
        }

    }
}
