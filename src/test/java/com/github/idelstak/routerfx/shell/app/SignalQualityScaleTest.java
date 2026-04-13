package com.github.idelstak.routerfx.shell.app;

import org.junit.jupiter.api.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

final class SignalQualityScaleTest {

    private final SignalQualityScale quality = new SignalQualityScale();

    @Test
    void rsrpMapsNoSignalLabelAtFloor() {
        assertThat("Expected RSRP floor to map to no signal label", quality.rsrp(-140d).level().label(), is("No signal"));
    }

    @Test
    void rsrpMapsExcellentLabelAtCeiling() {
        assertThat("Expected RSRP ceiling to map to excellent label", quality.rsrp(-44d).level().label(), is("Excellent"));
    }

    @Test
    void rsrpMapsGoodDescriptionAtGoodThreshold() {
        assertThat("Expected RSRP good band to expose the good description", quality.rsrp(-80d).level().description(), is("Reliable for most tasks"));
    }

    @Test
    void rsrpMapsGaugeToneFromThresholds() {
        assertThat("Expected RSRP poor band to expose poor gauge tone", quality.rsrp(-100d).level().gaugeTone(), is("signal-gauge-tone-poor"));
    }

    @Test
    void rssiMapsNoSignalChipToneAtNoSignalThreshold() {
        assertThat("Expected RSSI no-signal threshold to use no-signal chip tone", quality.rssi(-110d).level().chipTone(), is("metric-chip-tone-nosignal"));
    }

    @Test
    void rssiMapsExcellentRangeToneAtExcellentBand() {
        assertThat("Expected RSSI excellent band to use excellent range tone", quality.rssi(-70d).level().rangeTone(), is("metric-range-fill-excellent"));
    }

    @Test
    void sinrMapsFairLabelAtMiddleBand() {
        assertThat("Expected SINR middle band to map to moderate interference label", quality.sinr(5d).level().label(), is("Moderate interference"));
    }

    @Test
    void sinrMapsGoodLabelAtUpperMiddleBand() {
        assertThat("Expected SINR upper middle band to map to low interference label", quality.sinr(20d).level().label(), is("Low interference"));
    }

    @Test
    void rsrqMapsPoorLabelAtPoorBand() {
        assertThat("Expected RSRQ poor band to map to high congestion label", quality.rsrq(-15d).level().label(), is("High congestion"));
    }

    @Test
    void rsrqMapsExcellentLabelAboveTopBand() {
        assertThat("Expected RSRQ above-top values to map to clear channel label", quality.rsrq(-2d).level().label(), is("Clear channel"));
    }

    @Test
    void ratioIsClampedAtZeroBelowRssiMinimum() {
        assertThat("Expected RSSI ratio to clamp to zero below minimum", quality.rssi(-150d).ratio(), is(0d));
    }

    @Test
    void ratioIsClampedAtOneAboveRssiMaximum() {
        assertThat("Expected RSSI ratio to clamp to one above maximum", quality.rssi(-10d).ratio(), is(1d));
    }

    @Test
    void ratioUsesNormalizedPositionWithinRssiBand() {
        assertThat("Expected RSSI ratio to normalize values between min and max", quality.rssi(-80d).ratio(), closeTo(0.5d, 0.0001d));
    }
}
