package software.kes.gauntlet;

import software.kes.kraftwerk.GeneratorParameters;

final class SupplyParameters {
    private final GeneratorParameters generatorParameters;
    private final int maxDiscards;

    public static SupplyParameters supplyParameters(GeneratorParameters generatorParameters, int maxDiscards) {
        if (maxDiscards < 0) {
            maxDiscards = 0;
        }
        return new SupplyParameters(generatorParameters, maxDiscards);
    }

    private SupplyParameters(GeneratorParameters generatorParameters, int maxDiscards) {
        this.generatorParameters = generatorParameters;
        this.maxDiscards = maxDiscards;
    }

    public GeneratorParameters getGeneratorParameters() {
        return generatorParameters;
    }

    public int getMaxDiscards() {
        return maxDiscards;
    }
}
