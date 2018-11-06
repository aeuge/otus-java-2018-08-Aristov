package ru.otus.ATMDPT;


import java.util.*;

public class ATM implements ResetListener {
    private List<Cashbox> cashboxes = new ArrayList<>();
    private Caretaker caretaker = new Caretaker();

    ATM () throws AtmLoadException {
        try {
            loadCash();
            saveToMemento();
        } catch (AtmLoadException e) {
            throw new AtmLoadException(e.getMessage());
        }
    }

    public int getTotal() {
        int total = 0;
        for (Cashbox cb : cashboxes) {
            total += cb.getTotal();
        }
        return total;
    }

    public void saveToMemento() {
        caretaker.add(new Memento(cashboxes));
    }

    public void restoreFromMemento(int i) {
        cashboxes.clear();
        cashboxes.addAll(caretaker.get(i).getSavedState());
    }


    private List<Cashbox> getBanknotes () {
        List<Cashbox> banknotes = new ArrayList<>();
        for (Cashbox cb : cashboxes) {
            banknotes.add(new Cashbox(cb.getValue(),cb.getCount()));
        }
        return banknotes;
    }

    private String printBanknotes (List<Cashbox> banknotes) {
        String total = "";
        for (Cashbox cb : banknotes) {
            total += "купюра "+cb.getValue()+":"+cb.getCount()+";";
        }
        return total;
    }

    public String getStatus() {
        List<Cashbox> banknotes = getBanknotes();
        return printBanknotes(banknotes);
    }

    private List<Cashbox> tryWithdraw(int total) {
        List<Cashbox> withdraws = new ArrayList<>();
        for (Cashbox cb : cashboxes) {
            if (total>0) {
                int countvalue = (total / cb.getValue());
                if (countvalue>0) {
                    if (countvalue>cb.getCount()) {
                        countvalue = cb.getCount();
                    }
                    withdraws.add(new Cashbox(cb.getValue(),countvalue));
                    total -= countvalue*cb.getValue();
                }
            }
        }
        if (total>0){
            withdraws.clear();
        }
        return withdraws;
    }

    private String withdrawString (List<Cashbox> withdraws) {
        String stringwithdraw = "";
        for (Cashbox cb : withdraws) {
            stringwithdraw += "купюра "+cb.getValue()+":"+cb.getCount()+";";
        }
        return stringwithdraw;
    }

    private void withdrawCommit (List<Cashbox> withdraws) {
        for (Cashbox cb : cashboxes) {
            for (Cashbox cw : withdraws) {
                if (cw.equals(cb)) {
                    cb.decCount(cw.getCount());
                    break;
                }
            }
        }
    }

    public String withdraw(int total) throws AtmWithdrawException {
        if (total>0) {
            List<Cashbox> withdraws = tryWithdraw(total);

            if (withdraws.size()>0) {
                withdrawCommit (withdraws);//уменьшить количество купюр в банкомате
                return "выдача успешна "+withdrawString(withdraws);
            } else {
                throw new AtmWithdrawException("выдача суммы невозможна");
            }
        } else {
            throw new AtmWithdrawException("суммы не положительная");
        }
    }

    public void addCashbox (int value, int count)  {
        for (Cashbox cb : cashboxes) {
            if (cb.getValue()==value) {
               cb.addCount(count);
               count = 0;
               break;
            };
        }
        if (count>0) {
            cashboxes.add(new Cashbox(value,count));
            Collections.sort(cashboxes,Comparator.comparing(Cashbox::getValue).reversed());//сразу пачки денег сортируем от большей к меньшей
        }

    }

    @Override
    public void onReset() throws AtmLoadException{
        //сбрысываем на заводские
        restoreFromMemento(0);
    }

    private void loadCash() throws AtmLoadException {
        try {
            addCashbox(2000,(int) (Math.random() * 100));
            addCashbox(5000,(int) (Math.random() * 100));
            addCashbox(1000,(int) (Math.random() * 100));
            addCashbox(500,(int) (Math.random() * 100));
            addCashbox(200,(int) (Math.random() * 100));
            addCashbox(100,(int) (Math.random() * 100));
        } catch (Exception e) {
            throw new AtmLoadException(e.getMessage());
        }
    }
}
