package com.github.idelstak.routerfx.shell.app;

import java.util.*;

final class SignalQualityScale {

    private static final String RSRP_GAUGE_NO_SIGNAL = "signal-gauge-tone-nosignal";
    private static final String RSRP_GAUGE_POOR = "signal-gauge-tone-poor";
    private static final String RSRP_GAUGE_FAIR = "signal-gauge-tone-fair";
    private static final String RSRP_GAUGE_GOOD = "signal-gauge-tone-good";
    private static final String RSRP_GAUGE_EXCELLENT = "signal-gauge-tone-excellent";
    private static final String RSRP_CHIP_NO_SIGNAL = "signal-chip-tone-nosignal";
    private static final String RSRP_CHIP_POOR = "signal-chip-tone-poor";
    private static final String RSRP_CHIP_FAIR = "signal-chip-tone-fair";
    private static final String RSRP_CHIP_GOOD = "signal-chip-tone-good";
    private static final String RSRP_CHIP_EXCELLENT = "signal-chip-tone-excellent";
    private static final String METRIC_CHIP_NO_SIGNAL = "metric-chip-tone-nosignal";
    private static final String METRIC_CHIP_POOR = "metric-chip-tone-poor";
    private static final String METRIC_CHIP_FAIR = "metric-chip-tone-fair";
    private static final String METRIC_CHIP_GOOD = "metric-chip-tone-good";
    private static final String METRIC_CHIP_EXCELLENT = "metric-chip-tone-excellent";
    private static final String METRIC_RANGE_NO_SIGNAL = "metric-range-fill-nosignal";
    private static final String METRIC_RANGE_POOR = "metric-range-fill-poor";
    private static final String METRIC_RANGE_FAIR = "metric-range-fill-fair";
    private static final String METRIC_RANGE_GOOD = "metric-range-fill-good";
    private static final String METRIC_RANGE_EXCELLENT = "metric-range-fill-excellent";
    private final MetricScale rsrp;
    private final MetricScale rssi;
    private final MetricScale sinr;
    private final MetricScale rsrq;

    SignalQualityScale() {
        this.rsrp = new MetricScale(
          -140d,
          -44d,
          List.of(
            new QualityBand(-110d, new QualityLevel("No signal", "Connection unusable or dropped", RSRP_GAUGE_NO_SIGNAL, RSRP_CHIP_NO_SIGNAL, METRIC_RANGE_NO_SIGNAL)),
            new QualityBand(-100d, new QualityLevel("Poor", "Drops likely, very slow speeds", RSRP_GAUGE_POOR, RSRP_CHIP_POOR, METRIC_RANGE_POOR)),
            new QualityBand(-90d, new QualityLevel("Fair", "Usable but inconsistent", RSRP_GAUGE_FAIR, RSRP_CHIP_FAIR, METRIC_RANGE_FAIR)),
            new QualityBand(-80d, new QualityLevel("Good", "Reliable for most tasks", RSRP_GAUGE_GOOD, RSRP_CHIP_GOOD, METRIC_RANGE_GOOD)),
            new QualityBand(Double.POSITIVE_INFINITY, new QualityLevel("Excellent", "Strong, stable, fast speeds", RSRP_GAUGE_EXCELLENT, RSRP_CHIP_EXCELLENT, METRIC_RANGE_EXCELLENT))
          )
        );
        this.rssi = new MetricScale(
          -110d,
          -50d,
          List.of(
            new QualityBand(-110d, new QualityLevel("No signal", "", "", METRIC_CHIP_NO_SIGNAL, METRIC_RANGE_NO_SIGNAL)),
            new QualityBand(-100d, new QualityLevel("Poor", "", "", METRIC_CHIP_POOR, METRIC_RANGE_POOR)),
            new QualityBand(-90d, new QualityLevel("Fair", "", "", METRIC_CHIP_FAIR, METRIC_RANGE_FAIR)),
            new QualityBand(-80d, new QualityLevel("Good", "", "", METRIC_CHIP_GOOD, METRIC_RANGE_GOOD)),
            new QualityBand(Double.POSITIVE_INFINITY, new QualityLevel("Excellent", "", "", METRIC_CHIP_EXCELLENT, METRIC_RANGE_EXCELLENT))
          )
        );
        this.sinr = new MetricScale(
          -3d,
          20d,
          List.of(
            new QualityBand(-3d, new QualityLevel("No signal", "", "", METRIC_CHIP_NO_SIGNAL, METRIC_RANGE_NO_SIGNAL)),
            new QualityBand(0d, new QualityLevel("Poor", "", "", METRIC_CHIP_POOR, METRIC_RANGE_POOR)),
            new QualityBand(10d, new QualityLevel("Fair", "", "", METRIC_CHIP_FAIR, METRIC_RANGE_FAIR)),
            new QualityBand(20d, new QualityLevel("Good", "", "", METRIC_CHIP_GOOD, METRIC_RANGE_GOOD)),
            new QualityBand(Double.POSITIVE_INFINITY, new QualityLevel("Excellent", "", "", METRIC_CHIP_EXCELLENT, METRIC_RANGE_EXCELLENT))
          )
        );
        this.rsrq = new MetricScale(
          -20d,
          -5d,
          List.of(
            new QualityBand(-20d, new QualityLevel("No signal", "", "", METRIC_CHIP_NO_SIGNAL, METRIC_RANGE_NO_SIGNAL)),
            new QualityBand(-15d, new QualityLevel("Poor", "", "", METRIC_CHIP_POOR, METRIC_RANGE_POOR)),
            new QualityBand(-10d, new QualityLevel("Fair", "", "", METRIC_CHIP_FAIR, METRIC_RANGE_FAIR)),
            new QualityBand(-5d, new QualityLevel("Good", "", "", METRIC_CHIP_GOOD, METRIC_RANGE_GOOD)),
            new QualityBand(Double.POSITIVE_INFINITY, new QualityLevel("Excellent", "", "", METRIC_CHIP_EXCELLENT, METRIC_RANGE_EXCELLENT))
          )
        );
    }

    QualityValue rsrp(double value) {
        return rsrp.map(value);
    }

    QualityValue rssi(double value) {
        return rssi.map(value);
    }

    QualityValue sinr(double value) {
        return sinr.map(value);
    }

    QualityValue rsrq(double value) {
        return rsrq.map(value);
    }

    record QualityLevel(String label, String description, String gaugeTone, String chipTone, String rangeTone) {

    }

    record QualityValue(QualityLevel level, double ratio) {

    }

    private record MetricScale(double min, double max, List<QualityBand> bands) {

        private MetricScale {
            bands = List.copyOf(bands);
        }

        private QualityValue map(double value) {
            var ratio = ratio(value);
            for (var band : bands) {
                if (value <= band.max()) {
                    return new QualityValue(band.level(), ratio);
                }
            }
            return new QualityValue(bands.getLast().level(), ratio);
        }

        private double ratio(double value) {
            if (max <= min) {
                return 0d;
            }
            var clamped = Math.max(min, Math.min(max, value));
            var normalized = (clamped - min) / (max - min);
            return Math.max(0d, Math.min(1d, normalized));
        }
    }

    private record QualityBand(double max, QualityLevel level) {

    }
}
