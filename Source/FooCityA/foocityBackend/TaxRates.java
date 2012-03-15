package foocityBackend;

public class TaxRates {	

	private double sales_tax;
	private double property_tax;
	private double business_tax;
	private double occupation_tax;
	private double income_tax;

	TaxRates(double property, double sales, double business,
			double occupation, double income) {
		this.property_tax = property;
		this.sales_tax = sales;
		this.business_tax = business;
		this.occupation_tax = occupation;
		this.income_tax = income;
	}

	public double getProperty_tax() {
		return property_tax;
	}
	public double getSales_tax() {
		return sales_tax;
	}
	public double getBusiness_tax() {
		return business_tax;
	}
	public double getOccupation_tax() {
		return occupation_tax;
	}
	public double getIncome_tax() {
		return income_tax;
	}
	public void incrementProperty_tax() {
		if (this.property_tax < 19.51)
			this.property_tax += .5;
	}
	public void incrementSales_tax() {
		if (this.sales_tax < 19.51)
			this.sales_tax += .5;
	}
	public void incrementBusiness_tax() {
		if (this.business_tax < 19.51)
			this.business_tax += .5;
	}
	public void incrementOccupation_tax() {
		if (this.occupation_tax < 19.51)	
			this.occupation_tax += .5;
	}
	public void incrementIncome_tax() {
		if (this.income_tax < 19.51)
			this.income_tax += .5;
	}
	public void decrementProperty_tax() {
		if (this.property_tax > .49)
			this.property_tax -= .5;
	}
	public void decrementSales_tax() {
		if (this.sales_tax > .49)
			this.sales_tax -= .5;
	}
	public void decrementBusiness_tax() {
		if (this.business_tax > .49)
			this.business_tax -= .5;
	}
	public void decrementOccupation_tax() {
		if (this.occupation_tax > .49)	
			this.occupation_tax -= .5;
	}
	public void decrementIncome_tax() {
		if (this.income_tax > .49)
			this.income_tax -= .5;
	}
}
