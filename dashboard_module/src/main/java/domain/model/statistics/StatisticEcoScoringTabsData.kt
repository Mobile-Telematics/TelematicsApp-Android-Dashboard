package domain.model.statistics

internal data class StatisticEcoScoringTabsData(
    var week: StatisticEcoScoringTabData = StatisticEcoScoringTabData(),
    var month: StatisticEcoScoringTabData = StatisticEcoScoringTabData(),
    var year: StatisticEcoScoringTabData = StatisticEcoScoringTabData()
)