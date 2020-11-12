package DepManager;

public class ReadDepositeRow {
        private String Passport;
        private String FirstName;
        private String LastName;
        private String Ammount;
        private String Percent;
        private String PretermPercent;
        private String TermDays;
        private String StartDate;
        private String WithPercentCapitalization;


        public ReadDepositeRow() {
        }

        public ReadDepositeRow(String passport, String firstName, String lastName, String ammount, String percent, String pretermPercent, String termDays, String startDate, String withPercentCapitalization) {
            super();
            Passport = passport;
            FirstName = firstName;
            LastName = lastName;
            Ammount = ammount;
            Percent = percent;
            PretermPercent = pretermPercent;
            TermDays = termDays;
            StartDate = startDate;
            WithPercentCapitalization = withPercentCapitalization;
        }

        public String getPassport() {
            return Passport;
        }

        public void setPassport(String passport) {
            Passport = passport;
        }

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String firstName) {
            FirstName = firstName;
        }

        public String getLastName() {
            return LastName;
        }

        public void setLastName(String lastName) {
            LastName = lastName;
        }

        public String getAmmount() {
            return Ammount;
        }

        public void setAmmount(String ammount) {
            Ammount = ammount;
        }

        public String getPercent() {
            return Percent;
        }

        public void setPercent(String percent) {
            Percent = percent;
        }

        public String getPretermPercent() {
            return PretermPercent;
        }

        public void setPretermPercent(String pretermPercent) {
            PretermPercent = pretermPercent;
        }

        public String getTermDays() {
            return TermDays;
        }

        public void setTermDays(String termDays) {
            TermDays = termDays;
        }

        public String getStartDate() {
            return StartDate;
        }

        public void setStartDate(String startDate) {
            StartDate = startDate;
        }

        public String getWithPercentCapitalization() {
            return WithPercentCapitalization;
        }

        public void setWithPercentCapitalization(String withPercentCapitalization) {
            WithPercentCapitalization = withPercentCapitalization;
        }
}
