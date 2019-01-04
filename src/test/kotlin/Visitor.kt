interface ReportVisitable {
    fun accept(visitor: ReportVisitor)
}

class FixedPriceContract(val costPerYear: Long) : ReportVisitable {
    override fun accept(visitor: ReportVisitor) = visitor.visit(this)
}

class TimeAndMaterialsContract(val costPerHour: Long, val hours: Long) : ReportVisitable {
    override fun accept(visitor: ReportVisitor) = visitor.visit(this)
}

class SupportContract(val costPerMonth: Long) : ReportVisitable {
    override fun accept(visitor: ReportVisitor) = visitor.visit(this)
}

interface ReportVisitor {
    fun visit(contract: FixedPriceContract)
    fun visit(contract: TimeAndMaterialsContract)
    fun visit(contract: SupportContract)
}

class MonthlyCostReportVisitor(var monthlyCost: Long = 0) : ReportVisitor {
    override fun visit(contract: FixedPriceContract) {
        monthlyCost += contract.costPerYear / 12
    }

    override fun visit(contract: TimeAndMaterialsContract) {
        monthlyCost += contract.costPerHour * contract.hours
    }

    override fun visit(contract: SupportContract) {
        monthlyCost += contract.costPerMonth
    }
}

class YearlyReportVisitor(var yearlyCost: Long = 0) : ReportVisitor {
    override fun visit(contract: FixedPriceContract) {
        yearlyCost += contract.costPerYear
    }

    override fun visit(contract: TimeAndMaterialsContract) {
        yearlyCost += contract.costPerHour * contract.hours
    }

    override fun visit(contract: SupportContract) {
        yearlyCost += contract.costPerMonth * 12
    }
}

fun main(args: Array<String>) {
    val projectAlpha = FixedPriceContract(costPerYear = 10000)
    val projectBeta = SupportContract(costPerMonth = 500)
    val projectGamma = TimeAndMaterialsContract(hours = 150, costPerHour = 10)
    val projectKappa = TimeAndMaterialsContract(hours = 50, costPerHour = 50)

    val projects = arrayOf(projectAlpha, projectBeta, projectGamma, projectKappa)

    val monthlyCostReportVisitor = MonthlyCostReportVisitor()
    projects.forEach { it.accept(monthlyCostReportVisitor) }
    println("Monthly cost: ${monthlyCostReportVisitor.monthlyCost}")

    val yearlyReportVisitor = YearlyReportVisitor()
    projects.forEach { it.accept(yearlyReportVisitor) }
    println("Yearly cost: ${yearlyReportVisitor.yearlyCost}")
}
