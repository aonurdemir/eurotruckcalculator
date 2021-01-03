//Lets say
//        I can earn 30k € in one day
//        For each 100k € credit, I should pay 3k € daily
//        A truck is 150k € and each truck brings 15k € in a day.
//        You can get a credit in order to buy a truck. You cannot get a credit and store it.
//        You can only get 1 credit at a time
//
//        Find the minimum number of days to reach 500k € in cash

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MaxCash {
    int dailyEarn = 30;
    int creditPay = 3;
    int creditAmount = 100;
    int truckPrice = 150;
    int earnFromTruck = 10;

    HashMap<String, Integer> map = new HashMap<>();

    public int solve(int target, int truckCount, int day, int cash, int creditToPay, int creditCount, List<String> state) {
        String key = truckCount + "-" +
                cash + "-" +
                (creditToPay > 0 ? 1 : 0);


        if (map.containsKey(key)) {

            return map.get(key);
        }

        if(cash >= target && day == 57){
            System.out.println("++++++++++++");
            System.out.println("day: " + day);
            System.out.println("truck count: " + truckCount);
            System.out.println("credit to pay: " + creditToPay);
            for (String s: state){
                System.out.println(s);
            }
            System.out.println("-----------");
        }

        if (cash >= target) {

            return day;
        }

        int dailyIncome = dailyEarn + (truckCount * earnFromTruck) - (creditToPay > 0 ? creditPay : 0);
        if (creditToPay > 0) {
            creditToPay -= creditPay;
            creditToPay = Math.max(creditToPay, 0);
        }

        //no action
        state.add("NA - Cash: " +cash);
        int d1 = solve(target, truckCount, day + 1, cash + dailyIncome, creditToPay, creditCount, state);
        state.remove(state.size() - 1);

        //if I buy a truck
        int d2 = Integer.MAX_VALUE;
        int d3 = Integer.MAX_VALUE;
        if (cash >= truckPrice) {
            state.add(cash/truckPrice + " BuyWithCash - Cash: " + cash);
            d2 = solve(target, truckCount + (cash/truckPrice), day + 1, (cash % truckPrice) + dailyIncome, creditToPay, creditCount, state);
            state.remove(state.size() - 1);
        } else if (creditToPay == 0 && cash + creditAmount >= truckPrice) {
            int netCash = cash + creditAmount - truckPrice;
            state.add("BuyWithCredit - Cash: " + cash);
            d3 = solve(target, truckCount + 1, day + 1, netCash + dailyIncome, creditToPay + creditAmount, creditCount + 1, state);
            state.remove(state.size()-1);
        }

        map.put(key, Math.min(Math.min(d1, d2), d3));

        return map.get(key);

    }

    public static void main(String[] args) {
        MaxCash s = new MaxCash();
        int days = s.solve(10000, 5, 0, 40, 80, 0, new ArrayList<>());
        System.out.println(days);

    }
}
