package software.kes.gauntlet;

import software.kes.collectionviews.ImmutableNonEmptyVector;

import static com.jnape.palatable.lambda.adt.Unit.UNIT;

public final class DefaultReportRenderer implements ReportRenderer {
    private DefaultReportRenderer() {
    }

    public static DefaultReportRenderer defaultReportRenderer() {
        return new DefaultReportRenderer();
    }

    @Override
    public <A> String renderReport(ReportSettings settings, ReportData<A> reportData) {
        return reportData.getResult()
                .match(utr ->
                                utr.match(unfalsified -> renderReportForUnfalsified(settings, reportData, unfalsified),
                                        falsified -> renderReportForFalsified(settings, reportData, falsified)),
                        etr ->
                                etr.match(unproved -> renderReportForUnproved(settings, reportData, unproved),
                                        proved -> renderReportForProved(settings, reportData, proved)),
                        abnormal ->
                                abnormal.match(error -> renderReportForError(settings, reportData, error),
                                        exhausted -> renderReportForExhausted(settings, reportData, exhausted),
                                        timedOut -> renderReportForTimedOut(settings, reportData, timedOut),
                                        interrupted -> renderReportForInterrupted(settings, reportData, interrupted)));

    }

    private <A> String renderReportForUnfalsified(ReportSettings settings, ReportData<A> reportData, UniversalTestResult.Unfalsified<A> result) {
        return "";
    }

    private <A> String renderReportForProved(ReportSettings settings, ReportData<A> reportData, ExistentialTestResult.Proved<A> result) {
        return "";
    }

    private <A> String renderReportForFalsified(ReportSettings settings, ReportData<A> reportData, UniversalTestResult.Falsified<A> result) {
        PrettyPrinter<A> prettyPrinter = reportData.getPrettyPrinter();
        PrettyPrintParameters prettyPrintParameters = PrettyPrintParameters.prettyPrintParameters(settings.getFailureVerbosity());
        MutableReportBuilder output = new MutableReportBuilder();
        reportData.getTestParameterData().toOptional().ifPresent(tpd -> renderTestParameterData(tpd, output));
        output.write("Counterexample found after ");
        int successCount = result.getSuccessCount();
        output.write(successCount);
        output.write(successCount == 1 ? " success" : " successes");
        reportData.getInitialSeedValue().toOptional().ifPresent(seed -> {
            output.write(" using seed ");
            output.write(seed);
        });
        output.newLine();
        Counterexample<A> counterexample = result.getRefinedCounterexample()
                .match(__ -> result.getCounterexample(), RefinedCounterexample::getCounterexample);
        output.write("Counterexample: ");
        output.write(prettyPrinter.prettyPrint(prettyPrintParameters, counterexample.getSample()));
        output.newLine();
        result.getRefinedCounterexample().toOptional().ifPresent(rc -> {
            output.indent();
            output.write("(refined in ");
            output.write(rc.getShrinkCount());
            output.write(rc.getShrinkCount() == 1 ? " shrink" : " shrinks");
            output.write(" from original: ");
            output.write(prettyPrinter.prettyPrint(prettyPrintParameters, result.getCounterexample().getSample()));
            output.write(")");
            output.newLine();
            output.unindent();
        });
        writeEvalFailure(output, counterexample.getFailure(), 0);
        return output.render();
    }

    private void renderTestParameterData(TestParameterReportData data, MutableReportBuilder output) {
        output.write("For test created with parameter: ");
        output.write(data.getTestParameterValue());
        output.newLine();
        output.write("  (run ");
        output.write(data.getIndexInGroup().getOneBasedIndex());
        output.write(" of ");
        output.write(data.getIndexInGroup().getGroupSize());
        output.write(" in parameterized test group)");
        output.newLine();
    }

    private <A> String renderReportForUnproved(ReportSettings settings, ReportData<A> reportData, ExistentialTestResult.Unproved<A> result) {
        return "Property '" + reportData.getProp().getName() + "' remains unproved with the given data";
    }

    private <A> String renderReportForExhausted(ReportSettings settings, ReportData<A> reportData, Abnormal.Exhausted<A> result) {
        SupplyFailure supplyFailure = result.getSupplyFailure();
        int discardCount = supplyFailure.getDiscardCount();
        MutableReportBuilder output = new MutableReportBuilder();
        reportData.getTestParameterData().toOptional().ifPresent(tpd -> renderTestParameterData(tpd, output));
        output.write("Supply of samples exhausted after ");
        int successCount = result.getSuccessCount();
        output.write(successCount);
        output.write(successCount == 1 ? " success" : " successes");
        reportData.getInitialSeedValue().toOptional().ifPresent(seed -> {
            output.write(" using seed ");
            output.write(seed);
        });
        output.newLine();
        output.write("A filter caused ");
        output.write(discardCount);
        output.write(discardCount == 1 ? " sample" : " samples");
        output.writeLine(" to be discarded before giving up.");
        output.writeLine("You may need to redesign your filters.  Check your calls to 'suchThat'.");
        output.writeLine("The following may help you diagnose where the problem is:");
        output.newLine();
        writeSupplyTree(output, supplyFailure.getSupplyTree());

        return output.render();
    }

    private <A> String renderReportForError(ReportSettings settings, ReportData<A> reportData, Abnormal.Error<A> result) {
        return "Test threw an error: " + result.getError();
    }

    private <A> String renderReportForTimedOut(ReportSettings settings, ReportData<A> reportData, Abnormal.TimedOut<A> result) {
        return "Timed out";
    }

    private <A> String renderReportForInterrupted(ReportSettings settings, ReportData<A> reportData, Abnormal.Interrupted<A> result) {
        return "Interrupted";
    }

    private void writeEvalFailure(MutableReportBuilder output, EvalFailure failure, int depth) {
        writeProperty(output, failure.getPropertyName(), depth);
        writeReasons(output, failure.getReasons(), depth);
        output.indent();
        try {
            for (Cause cause : failure.getCauses()) {
                cause.match(explanation -> {
                            writeExplanation(output, explanation, depth + 1);
                            return UNIT;
                        },
                        propertyFailed -> {
                            writeEvalFailure(output, propertyFailed.getFailure(), depth + 1);
                            return UNIT;
                        });
            }
        } finally {
            output.unindent();
        }
    }

    private void writeExplanation(MutableReportBuilder output, Cause.Explanation explanation, int depth) {
        writeProperty(output, explanation.getPropertyName(), depth);
        writeReasons(output, explanation.getReasons(), depth);
    }

    private void writeProperty(MutableReportBuilder output, Named property, int depth) {
        if (depth > 0) {
            output.write("- ");
        }
        output.write("Property: \"");
        output.write(property.getName());
        output.write("\"");
        output.newLine();
    }

    private void writeReasons(MutableReportBuilder output, Reasons reasons, int depth) {
        if (depth > 0) {
            output.write("  ");
        }
        ImmutableNonEmptyVector<String> items = reasons.getItems();
        if (items.size() == 1) {
            output.write("Reason: ");
        } else {
            output.write("Reasons: ");
        }
        boolean inner = false;
        for (String reason : items) {
            if (inner) {
                output.write("; ");
            } else {
                inner = true;
            }
            output.write(reason);
        }
        output.newLine();
    }

    private void writeSupplyTree(MutableReportBuilder output, SupplyTree supplyTree) {
        supplyTree.match(
                leaf -> {
                    output.writeLine("- " + leaf.getLabel());
                    return UNIT;
                },
                composite -> {
                    output.writeLine("- Composite of [");
                    output.indent();
                    composite.getChildren().forEach(st -> writeSupplyTree(output, st));
                    output.unindent();
                    output.writeLine("  ]");
                    return UNIT;
                },
                collection -> {
                    output.writeLine("- Collection of [");
                    output.indent();
                    writeSupplyTree(output, collection.getChild());
                    output.unindent();
                    output.writeLine("  ]");
                    return UNIT;
                },
                mapping -> {
                    output.writeLine("- Mapping of [");
                    output.indent();
                    writeSupplyTree(output, mapping.getUnderlying());
                    output.unindent();
                    output.writeLine("  ]");
                    return UNIT;
                },
                filter -> {
                    output.writeLine("- Filter of [");
                    output.indent();
                    writeSupplyTree(output, filter.getUnderlying());
                    output.unindent();
                    output.writeLine("  ]");
                    return UNIT;
                },
                exhausted -> {
                    int attemptCount = exhausted.getAttemptCount();
                    String attempts = "" + attemptCount + " " + (attemptCount == 1 ? "attempt" : "attempts");
                    output.writeLine("*** Filter failed here after " + attempts + "[");
                    output.indent();
                    writeSupplyTree(output, exhausted.getUnderlying());
                    output.unindent();
                    output.writeLine("  ]");
                    return UNIT;
                });
    }
}
