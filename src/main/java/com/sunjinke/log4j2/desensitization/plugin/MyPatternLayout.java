package com.sunjinke.log4j2.desensitization.plugin;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.*;
import org.apache.logging.log4j.core.layout.*;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;
import org.apache.logging.log4j.util.Strings;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * 创建时间 2019年三月28日 星期四 10:30
 * 作者: zhangjunping
 * 描述：自定义脱敏的log4j2 Layout插件
 */
@Plugin(name = "MyPatternLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public final class MyPatternLayout extends AbstractStringLayout{

    /**
     * Default pattern string for log output. Currently set to the string <b>"%m%n"</b> which just prints the
     * application supplied message.
     */
    public static final String DEFAULT_CONVERSION_PATTERN = "%m%n";

    /** Key to identify pattern converters. */
    public static final String KEY = "Converter";

    /**
     * Conversion pattern.
     */
    private final String conversionPattern;
    private final PatternSelector patternSelector;
    private final Serializer eventSerializer;

    /**
     * Constructs a PatternLayout using the supplied conversion pattern.
     *
     * @param config The Configuration.
     * @param replace The regular expression to match.
     * @param eventPattern conversion pattern.
     * @param patternSelector The PatternSelector.
     * @param charset The character set.
     * @param alwaysWriteExceptions Whether or not exceptions should always be handled in this pattern (if {@code true},
     *                         exceptions will be written even if the pattern does not specify so).
     * @param noConsoleNoAnsi
     *            If {@code "true"} (default) and {@link System#console()} is null, do not output ANSI escape codes
     * @param headerPattern header conversion pattern.
     * @param footerPattern footer conversion pattern.
     */
    private MyPatternLayout(final Configuration config, final JsonRegexReplaces replace, final String eventPattern,
                          final PatternSelector patternSelector, final Charset charset, final boolean alwaysWriteExceptions,
                          final boolean noConsoleNoAnsi, final String headerPattern, final String footerPattern) {
        super(config, charset,
                createSerializer(config, replace, headerPattern, null, patternSelector, alwaysWriteExceptions,
                        noConsoleNoAnsi),
                createSerializer(config, replace, footerPattern, null, patternSelector, alwaysWriteExceptions,
                        noConsoleNoAnsi));
        this.conversionPattern = eventPattern;
        this.patternSelector = patternSelector;
        this.eventSerializer = createSerializer(config, replace, eventPattern, DEFAULT_CONVERSION_PATTERN,
                patternSelector, alwaysWriteExceptions, noConsoleNoAnsi);
    }

    public static Serializer createSerializer(final Configuration configuration, final JsonRegexReplaces replace,
                                              final String pattern, final String defaultPattern, final PatternSelector patternSelector,
                                              final boolean alwaysWriteExceptions, final boolean noConsoleNoAnsi) {
        if (Strings.isEmpty(pattern) && Strings.isEmpty(defaultPattern)) {
            return null;
        }
        if (patternSelector == null) {
            try {
                final PatternParser parser = createPatternParser(configuration);
                final List<PatternFormatter> list = parser.parse(pattern == null ? defaultPattern : pattern,
                        alwaysWriteExceptions, noConsoleNoAnsi);
                final PatternFormatter[] formatters = list.toArray(new PatternFormatter[0]);
                return new MyPatternLayout.PatternSerializer(formatters, replace);
            } catch (final RuntimeException ex) {
                throw new IllegalArgumentException("Cannot parse pattern '" + pattern + "'", ex);
            }
        }
        return new MyPatternLayout.PatternSelectorSerializer(patternSelector, replace);
    }

    /**
     * Gets the conversion pattern.
     *
     * @return the conversion pattern.
     */
    public String getConversionPattern() {
        return conversionPattern;
    }


    /**
     * Formats a logging event to a writer.
     *
     * @param event logging event to be formatted.
     * @return The event formatted as a String.
     */
    @Override
    public String toSerializable(final LogEvent event) {
        return eventSerializer.toSerializable(event);
    }

    @Override
    public void encode(final LogEvent event, final ByteBufferDestination destination) {
        if (!(eventSerializer instanceof Serializer2)) {
            super.encode(event, destination);
            return;
        }
        final StringBuilder text = toText((Serializer2) eventSerializer, event, getStringBuilder());
        final Encoder<StringBuilder> encoder = getStringBuilderEncoder();
        encoder.encode(text, destination);
        trimToMaxSize(text);
    }

    /**
     * Creates a text representation of the specified log event
     * and writes it into the specified StringBuilder.
     * <p>
     * Implementations are free to return a new StringBuilder if they can
     * detect in advance that the specified StringBuilder is too small.
     */
    private StringBuilder toText(final Serializer2 serializer, final LogEvent event,
                                 final StringBuilder destination) {
        return serializer.toSerializable(event, destination);
    }

    /**
     * Creates a PatternParser.
     * @param config The Configuration.
     * @return The PatternParser.
     */
    public static PatternParser createPatternParser(final Configuration config) {
        if (config == null) {
            return new PatternParser(config, KEY, LogEventPatternConverter.class);
        }
        PatternParser parser = config.getComponent(KEY);
        if (parser == null) {
            parser = new PatternParser(config, KEY, LogEventPatternConverter.class);
            config.addComponent(KEY, parser);
            parser = config.getComponent(KEY);
        }
        return parser;
    }

    @Override
    public String toString() {
        return patternSelector == null ? conversionPattern : patternSelector.toString();
    }

    /**
     * Creates a pattern layout.
     *
     * @param pattern
     *        The pattern. If not specified, defaults to DEFAULT_CONVERSION_PATTERN.
     * @param patternSelector
     *        Allows different patterns to be used based on some selection criteria.
     * @param config
     *        The Configuration. Some Converters require access to the Interpolator.
     * @param replace
     *        A Regex replacement String.
     * @param charset
     *        The character set. The platform default is used if not specified.
     * @param alwaysWriteExceptions
     *        If {@code "true"} (default) exceptions are always written even if the pattern contains no exception tokens.
     * @param noConsoleNoAnsi
     *        If {@code "true"} (default is false) and {@link System#console()} is null, do not output ANSI escape codes
     * @param headerPattern
     *        The footer to place at the top of the document, once.
     * @param footerPattern
     *        The footer to place at the bottom of the document, once.
     * @return The PatternLayout.
     */
    @PluginFactory
    public static MyPatternLayout createLayout(
            @PluginAttribute(value = "pattern", defaultString = DEFAULT_CONVERSION_PATTERN) final String pattern,
            @PluginElement("PatternSelector") final PatternSelector patternSelector,
            @PluginConfiguration final Configuration config,
            @PluginElement("jsonReplaces") final JsonRegexReplaces replace,
            // LOG4J2-783 use platform default by default, so do not specify defaultString for charset
            @PluginAttribute(value = "charset") final Charset charset,
            @PluginAttribute(value = "alwaysWriteExceptions", defaultBoolean = true) final boolean alwaysWriteExceptions,
            @PluginAttribute(value = "noConsoleNoAnsi", defaultBoolean = false) final boolean noConsoleNoAnsi,
            @PluginAttribute("header") final String headerPattern,
            @PluginAttribute("footer") final String footerPattern) {
        return newBuilder()
                .withPattern(pattern)
                .withPatternSelector(patternSelector)
                .withConfiguration(config)
                .withRegexReplacement(replace)
                .withCharset(charset)
                .withAlwaysWriteExceptions(alwaysWriteExceptions)
                .withNoConsoleNoAnsi(noConsoleNoAnsi)
                .withHeader(headerPattern)
                .withFooter(footerPattern)
                .build();
    }

    private static class PatternSerializer implements Serializer, Serializer2 {

        private final PatternFormatter[] formatters;
        private final JsonRegexReplaces replace;

        private PatternSerializer(final PatternFormatter[] formatters, final JsonRegexReplaces replace) {
            super();
            this.formatters = formatters;
            this.replace = replace;
        }

        @Override
        public String toSerializable(final LogEvent event) {
            final StringBuilder sb = getStringBuilder();
            try {
                return toSerializable(event, sb).toString();
            } finally {
                trimToMaxSize(sb);
            }
        }

        @Override
        public StringBuilder toSerializable(final LogEvent event, final StringBuilder buffer) {
            final int len = formatters.length;
            for (int i = 0; i < len; i++) {
                formatters[i].format(event, buffer);
            }
            if (replace != null) { // creates temporary objects
                String str = buffer.toString();
                str = replace.format(str);
                buffer.setLength(0);
                buffer.append(str);
            }
            return buffer;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append(super.toString());
            builder.append("[formatters=");
            builder.append(Arrays.toString(formatters));
            builder.append(", replace=");
            builder.append(replace);
            builder.append("]");
            return builder.toString();
        }
    }

    private static class PatternSelectorSerializer implements Serializer, Serializer2 {

        private final PatternSelector patternSelector;
        private final JsonRegexReplaces replace;

        private PatternSelectorSerializer(final PatternSelector patternSelector, final JsonRegexReplaces replace) {
            super();
            this.patternSelector = patternSelector;
            this.replace = replace;
        }

        @Override
        public String toSerializable(final LogEvent event) {
            final StringBuilder sb = getStringBuilder();
            try {
                return toSerializable(event, sb).toString();
            } finally {
                trimToMaxSize(sb);
            }
        }

        @Override
        public StringBuilder toSerializable(final LogEvent event, final StringBuilder buffer) {
            final PatternFormatter[] formatters = patternSelector.getFormatters(event);
            final int len = formatters.length;
            for (int i = 0; i < len; i++) {
                formatters[i].format(event, buffer);
            }
            if (replace != null) { // creates temporary objects
                String str = buffer.toString();
                str = replace.format(str);
                buffer.setLength(0);
                buffer.append(str);
            }
            return buffer;
        }

        @Override
        public String toString() {
            return  super.toString() +
                    "[patternSelector=" +
                    patternSelector +
                    ", replaces=" +
                    replace +
                    "]";
        }
    }

    /**
     * Creates a PatternLayout using the default options. These options include using UTF-8, the default conversion
     * pattern, exceptions being written, and with ANSI escape codes.
     *
     * @return the PatternLayout.
     * @see #DEFAULT_CONVERSION_PATTERN Default conversion pattern
     */
    public static MyPatternLayout createDefaultLayout() {
        return newBuilder().build();
    }

    /**
     * Creates a PatternLayout using the default options and the given configuration. These options include using UTF-8,
     * the default conversion pattern, exceptions being written, and with ANSI escape codes.
     *
     * @param configuration The Configuration.
     *
     * @return the PatternLayout.
     * @see #DEFAULT_CONVERSION_PATTERN Default conversion pattern
     */
    public static MyPatternLayout createDefaultLayout(final Configuration configuration) {
        return newBuilder().withConfiguration(configuration).build();
    }

    /**
     * Creates a builder for a custom PatternLayout.
     *
     * @return a PatternLayout builder.
     */
    @PluginBuilderFactory
    public static MyPatternLayout.Builder newBuilder() {
        return new MyPatternLayout.Builder();
    }

    /**
     * Custom PatternLayout builder. Use the {@link PatternLayout#newBuilder() builder factory method} to create this.
     */
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<MyPatternLayout> {

        @PluginBuilderAttribute
        private String pattern = PatternLayout.DEFAULT_CONVERSION_PATTERN;

        @PluginElement("PatternSelector")
        private PatternSelector patternSelector;

        @PluginConfiguration
        private Configuration configuration;

        @PluginElement("jsonReplaces")
        private JsonRegexReplaces regexReplacement;

        // LOG4J2-783 use platform default by default
        @PluginBuilderAttribute
        private Charset charset = Charset.defaultCharset();

        @PluginBuilderAttribute
        private boolean alwaysWriteExceptions = true;

        @PluginBuilderAttribute
        private boolean noConsoleNoAnsi;

        @PluginBuilderAttribute
        private String header;

        @PluginBuilderAttribute
        private String footer;

        private Builder() {
        }

        // TODO: move javadocs from PluginFactory to here

        public MyPatternLayout.Builder withPattern(final String pattern) {
            this.pattern = pattern;
            return this;
        }

        public MyPatternLayout.Builder withPatternSelector(final PatternSelector patternSelector) {
            this.patternSelector = patternSelector;
            return this;
        }

        public MyPatternLayout.Builder withConfiguration(final Configuration configuration) {
            this.configuration = configuration;
            return this;
        }

        public MyPatternLayout.Builder withRegexReplacement(final JsonRegexReplaces regexReplacement) {
            this.regexReplacement = regexReplacement;
            return this;
        }

        public MyPatternLayout.Builder withCharset(final Charset charset) {
            // LOG4J2-783 if null, use platform default by default
            if (charset != null) {
                this.charset = charset;
            }
            return this;
        }

        public MyPatternLayout.Builder withAlwaysWriteExceptions(final boolean alwaysWriteExceptions) {
            this.alwaysWriteExceptions = alwaysWriteExceptions;
            return this;
        }

        public MyPatternLayout.Builder withNoConsoleNoAnsi(final boolean noConsoleNoAnsi) {
            this.noConsoleNoAnsi = noConsoleNoAnsi;
            return this;
        }

        public MyPatternLayout.Builder withHeader(final String header) {
            this.header = header;
            return this;
        }

        public MyPatternLayout.Builder withFooter(final String footer) {
            this.footer = footer;
            return this;
        }

        @Override
        public MyPatternLayout build() {
            if (configuration == null) {
                configuration = new DefaultConfiguration();
            }
            return new MyPatternLayout(configuration, regexReplacement, pattern, patternSelector, charset,
                    alwaysWriteExceptions, noConsoleNoAnsi, header, footer);
        }
    }
}
