#!/usr/bin/env Rscript

# Using the mana curve suggested at https://f2k.gg/articles/2387_mtg-arena-introduction-to-the-sealed-deck-format
ideal.mana.curve <- c(
    1,
    2, 2, 2, 2, 2,
    3, 3, 3, 3,
    4, 4, 4, 4,
    5, 5, 5,
    6, 6)

ideal.mana.curve.low <- c(
    2, 2, 2, 2,
    3, 3, 3,
    4, 4, 4, 4,
    5, 5, 5,
    6, 6)

ideal.mana.curve.high <- c(
    1, 1,
    2, 2, 2, 2, 2, 2,
    3, 3, 3, 3, 3,
    4, 4, 4, 4,
    5, 5, 5,
    6, 6)

acceptable.mana.curve <- c(
    1, 1,
    2, 2, 2, 2, 2, 2,
    3, 3, 3,
    4, 4, 4, 4,
    5, 5, 5,
    6, 6,
    7, 7,
    8, 8)

bad.mana.curve <- c(
    1,
    2,
    3,
    4, 4, 4, 4,
    5, 5, 5, 5, 5, 5,
    6, 6,
    7, 7,
    8, 8, 8, 8, 8, 8)

costs <- c(1:8)

#### Plots ####
# Relative Frequency Histogram
hist(ideal.mana.curve, right = FALSE, prob = TRUE,
     main = "Ideal Mana Curve: Relative Frequency")

# Density plot
plot(density(ideal.mana.curve),
     main = "Ideal Mana Curve: Density Plot")

#### Idea: Relative frequency ####

ideal.low.relative.frequency <- table(ideal.mana.curve.low) / length(ideal.mana.curve.low)
ideal.relative.frequency <- table(ideal.mana.curve) / length(ideal.mana.curve)
ideal.high.relative.frequency <- table(ideal.mana.curve.high) / length(ideal.mana.curve.high)
acceptable.relative.frequency <- table(acceptable.mana.curve) / length(acceptable.mana.curve)
bad.relative.frequency <- table(bad.mana.curve) / length(bad.mana.curve)

#### Idea: Cumulative distribution function ####

# Ideal:
#   <= 1 CMC: 5% ± 5%
#   <= 2 CMC: 31% ± 5%
#   <= 3 CMC: 52% ± 5%
#   <= 4 CMC: 73% ± 5%
#   <= 5 CMC: 89% ± 5%

# cumsum(ideal.relative.frequency)
# cumsum(ideal.low.relative.frequency)
# cumsum(ideal.high.relative.frequency)
# cumsum(acceptable.relative.frequency)
# cumsum(bad.relative.frequency)

ecdf(ideal.mana.curve.low)(costs)
ecdf(ideal.mana.curve)(costs)
ecdf(ideal.mana.curve.high)(costs)
ecdf(acceptable.mana.curve)(costs)
ecdf(bad.mana.curve)(costs)

#### Idea: Percentiles ####
# Ideal:
#   16th percentile: 2 ± 1 converted mana cost
#   32th percentile: 2.76 ± 1 converted mana cost
#   48th percentile: 3 ± 1 converted mana cost
#   64th percentile: 4 ± 1 converted mana cost
#   80th percentile: 5 ± 1 converted mana cost
#   96th percentile: 6 ± 1 converted mana cost

# quantiles.of.interest <- c(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9)
quantiles.of.interest <- c(0.16, 0.32, 0.48, 0.64, 0.80, 0.96)
# quantiles.of.interest <- c(0.20, 0.40, 0.60, 0.80)

# Ideal mana curves
quantile(ideal.mana.curve.low, probs = quantiles.of.interest)
quantile(ideal.mana.curve, probs = quantiles.of.interest)
quantile(ideal.mana.curve.high, probs = quantiles.of.interest)

# Ok mana curves
quantile(acceptable.mana.curve, probs = quantiles.of.interest)

# Bad mana curves
quantile(bad.mana.curve, probs = quantiles.of.interest)
