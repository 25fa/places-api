package com.banana.telescope.worker

class SimilarityWorker {
    fun similarity(source: String, target: String): Double {
        var longer = source
        var shorter = target
        if (source.length < target.length) {
            longer = target
            shorter = source
        }
        val longerLength = longer.length
        return if (longerLength == 0) {
            1.0
        } else {
            (longerLength - editDistance(longer, shorter)) / longerLength.toDouble()
        }
    }

    private fun editDistance(source: String, target: String): Int {

        val costs = IntArray(target.length + 1)
        for (i in 0..source.length) {
            var lastValue = i
            for (j in 0..target.length) {
                if (i == 0) {
                    costs[j] = j
                } else {
                    if (j > 0) {
                        var newValue = costs[j - 1]
                        if (source[i - 1] != target[j - 1]) {
                            newValue = newValue.coerceAtMost(lastValue).coerceAtMost(costs[j]) + 1
                        }
                        costs[j - 1] = lastValue
                        lastValue = newValue
                    }
                }
            }
            if (i > 0) costs[target.length] = lastValue
        }
        return costs[target.length]
    }
}