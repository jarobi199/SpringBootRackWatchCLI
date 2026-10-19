package io.rackwatch.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Renders a horizontal ASCII bar chart in the terminal using plain Java.
 *
 * Usage (currency — unchanged default behavior):
 *   BarChartUtil.builder()
 *       .title("Value by Item Type")
 *       .bar("Electronic", 8750.00)
 *       .bar("Furniture", 3100.00)
 *       .bar("Appliance", 1450.00)
 *       .showTotal(true)   // prepends a Total bar — sum of all bars, always full width
 *       .maxWidth(40)
 *       .render();
 *
 * Usage (non-currency value, with an optional suffix shown but not charted):
 *   BarChartUtil.builder()
 *       .title("Average Rating by Producer")
 *       .valueFormat("%.1f")
 *       .bar("Domaine Leroux", 91.0, "3 tastings")
 *       .bar("Maison Belliard", 88.0, "6 tastings")
 *       .render();
 */
public class BarChartUtil {

    private final String title;
    private final Map<String, Double> bars;
    private final Map<String, String> suffixes;
    private final String valueFormat;
    private final int maxWidth;
    private final boolean showTotal;

    private BarChartUtil(Builder builder) {
        this.title = builder.title;
        this.bars = builder.bars;
        this.suffixes = builder.suffixes;
        this.valueFormat = builder.valueFormat;
        this.maxWidth = builder.maxWidth;
        this.showTotal = builder.showTotal;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void render() {
        if (bars.isEmpty()) return;

        double total = bars.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        // When showTotal is true, Total is always the max (full bar).
        // Otherwise scale to the largest individual bar.
        double maxValue = showTotal ? total
                : bars.values().stream().mapToDouble(Double::doubleValue).max().orElse(1.0);

        if (title != null && !title.isBlank()) {
            System.out.println("\n" + title);
            System.out.println("─".repeat(title.length()));
        }

        // Label width accounts for "Total" if shown
        int labelWidth = bars.keySet().stream()
                .mapToInt(String::length)
                .max()
                .orElse(10);
        if (showTotal) {
            labelWidth = Math.max(labelWidth, "Total".length());
        }

        // Print Total bar first if requested — never carries a suffix, it's a sum, not an entry.
        if (showTotal) {
            printBar("Total", total, maxValue, labelWidth, null);
        }

        for (Map.Entry<String, Double> entry : bars.entrySet()) {
            printBar(entry.getKey(), entry.getValue(), maxValue, labelWidth, suffixes.get(entry.getKey()));
        }

        System.out.println();
    }

    public void printBar(String label, double value, double maxValue, int labelWidth, String suffix) {
        printBar(label, value, maxValue,  labelWidth, suffix, false);
    }

    public void printBar(String label, double value, double maxValue, int labelWidth, String suffix, boolean displayValue) {
        int filled = (int) Math.round((value / maxValue) * maxWidth);
        int empty = maxWidth - filled;
        String bar = "█".repeat(filled) + "░".repeat(empty);
        String formattedLabel = String.format("%-" + labelWidth + "s", label);
        String formattedValue = String.format(valueFormat, value);
        String line = formattedLabel + "  " + bar + (displayValue ? " " + formattedValue : "");
        if (suffix != null && !suffix.isBlank()) {
            line += "  (" + suffix + ")";
        }
        System.out.println(line);
    }

    // ── Builder ──────────────────────────────────────────────────────────────

    public static class Builder {

        private String title = null;
        private final Map<String, Double> bars = new LinkedHashMap<>();
        private final Map<String, String> suffixes = new LinkedHashMap<>();
        private String valueFormat = "$%,.2f";
        private int maxWidth = 40;
        private boolean showTotal = false;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder bar(String label, double value) {
            bars.put(label, value);
            return this;
        }

        /**
         * Same as bar(label, value), but attaches a suffix shown in parentheses
         * after the formatted value — e.g. a tasting count. The suffix is display
         * only; it plays no part in sizing the bar.
         */
        public Builder bar(String label, double value, String suffix) {
            bars.put(label, value);
            suffixes.put(label, suffix);
            return this;
        }

        /**
         * Prepends a Total bar summing all bars. It is always full width (the scale reference).
         */
        public Builder showTotal(boolean showTotal) {
            this.showTotal = showTotal;
            return this;
        }

        /**
         * Format string used for the value printed after each bar (default "$%,.2f").
         * Pass a plain numeric format — e.g. "%.1f" for a rating out of 100 — for
         * charts that aren't currency.
         */
        public Builder valueFormat(String valueFormat) {
            this.valueFormat = valueFormat;
            return this;
        }

        /**
         * Max character width of the bar portion (default 40).
         */
        public Builder maxWidth(int maxWidth) {
            this.maxWidth = Math.max(10, maxWidth);
            return this;
        }

        public BarChartUtil build() {
            return new BarChartUtil(this);
        }

        /**
         * Convenience — build and render in one call.
         */
        public void render() {
            build().render();
        }

        public void printRatioBar(String label, double value, double maxValue) {
            build().printBar(label, value, maxValue, 20, value + "/" + maxValue);
        }
    }

    public static void main(String[] args) {
        BarChartUtil.builder().valueFormat("%.1f").printRatioBar("Ratio", 3.0, 10.0);
    }
}
