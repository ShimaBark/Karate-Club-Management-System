package BusinessLayer;
import java.time.LocalDate;
public class SubscriptionPeriod {
    private enum enPeriodType {MONTH, HALF_YEAR, YEAR}

    ;
    private LocalDate startDate;
    private LocalDate endDate;
    private enPeriodType periodType;
    private double fees;

    public int getPeriodType() {
        return periodType.ordinal();
    }

    public void setPeriodType(enPeriodType periodType) {
        this.periodType = periodType;
    }

    public SubscriptionPeriod(LocalDate startDate, int period) {

        this.startDate = startDate;
        periodType = enPeriodType.values()[period];
        switch (periodType) {
            case enPeriodType.MONTH -> {
                this.endDate =
                        startDate.plusMonths(1);
                this.fees = 1000;


            }
            case enPeriodType.HALF_YEAR -> {
                this.endDate =
                        startDate.plusMonths(6);
                this.fees = 5000;

            }
            case enPeriodType.YEAR -> {
                this.endDate =
                        startDate.plusYears(1);
                this.fees = 8000;

            }


        }
    }

    public SubscriptionPeriod(LocalDate startDate, LocalDate endDate,int periodType ,double fees) {
        this.endDate = endDate;
        this.startDate = startDate;
        this.fees = fees;
        this.periodType=enPeriodType.values()[periodType];

    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getFees() {
        return fees;
    }


}
