package org.streampipes.pe.mixed.flink.samples.measurementUnitConverter;

import com.github.jqudt.Unit;
import org.streampipes.model.graph.DataProcessorDescription;
import org.streampipes.model.graph.DataProcessorInvocation;
import org.streampipes.model.output.OutputStrategy;
import org.streampipes.pe.mixed.flink.samples.FlinkConfig;
import org.streampipes.sdk.builder.ProcessingElementBuilder;
import org.streampipes.sdk.extractor.ProcessingElementParameterExtractor;
import org.streampipes.sdk.helpers.EpRequirements;
import org.streampipes.sdk.helpers.OutputStrategies;
import org.streampipes.sdk.helpers.SupportedFormats;
import org.streampipes.sdk.helpers.SupportedProtocols;
import org.streampipes.units.UnitProvider;
import org.streampipes.wrapper.flink.FlinkDataProcessorDeclarer;
import org.streampipes.model.staticproperty.Option;
import org.streampipes.wrapper.flink.FlinkDataProcessorRuntime;

import java.util.LinkedList;
import java.util.List;

public class MeasurementUnitConverterController extends FlinkDataProcessorDeclarer<MeasurementUnitConverterParameters> {

    private static final String UNIT_NAME = "unitName";
    private static final String INPUT_UNIT = "inputUnit";
    private static final String OUTPUT_UNIT = "outputUnit";

    @Override
    public DataProcessorDescription declareModel() {
        List<Unit> availableUnits = UnitProvider.INSTANCE.getAvailableUnits();
        List<Option> optionsListInput = new LinkedList<>();
        List<Option> optionsListOutput = new LinkedList<>();
        availableUnits.forEach(unit -> {
                optionsListInput.add(new Option(unit.getLabel()));
                optionsListOutput.add(new Option(unit.getLabel()));
                }
        );


        return ProcessingElementBuilder.create("measurement_unit_converter", "Measurement Unit Converter",
                "Converts a unit of measurement to another one")
                .requiredPropertyStream1(EpRequirements.anyProperty())
                .requiredTextParameter(UNIT_NAME, "Unit name",
                        "The name of the unit which should convert")
                .requiredSingleValueSelection(INPUT_UNIT, "Input type",
                        "The input type unit of ", optionsListInput)
                .requiredSingleValueSelection(OUTPUT_UNIT, "Output type",
                        "The output type unit of measurement", optionsListOutput)
                .supportedProtocols(SupportedProtocols.kafka())
                .supportedFormats(SupportedFormats.jsonFormat())
                .outputStrategy(OutputStrategies.keep())
                .build();
    }



    @Override
    protected FlinkDataProcessorRuntime<MeasurementUnitConverterParameters> getRuntime(DataProcessorInvocation sepa) {
        ProcessingElementParameterExtractor extractor = ProcessingElementParameterExtractor.from(sepa);

        String unitName = extractor.singleValueParameter(UNIT_NAME, String.class);
        String inputUnitName = extractor.selectedSingleValue(INPUT_UNIT, String.class);
        String outputUnityName =  extractor.selectedSingleValue(OUTPUT_UNIT, String.class);

        Unit inputUnit = UnitProvider.INSTANCE.getUnitByLabel(inputUnitName);
        Unit outputUnit = UnitProvider.INSTANCE.getUnitByLabel(outputUnityName);


        MeasurementUnitConverterParameters staticParams = new MeasurementUnitConverterParameters(
                sepa,
                unitName,
                inputUnit,
                outputUnit
        );


        //return new MeasurementUnitConverterProgram(staticParams,  new FlinkDeploymentConfig(FlinkConfig.JAR_FILE,
        //        FlinkConfig.INSTANCE.getFlinkHost(), FlinkConfig.INSTANCE.getFlinkPort()));
        return new MeasurementUnitConverterProgram(staticParams);
    }


}