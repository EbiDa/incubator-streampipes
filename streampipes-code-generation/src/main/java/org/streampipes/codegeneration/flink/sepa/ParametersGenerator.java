package org.streampipes.codegeneration.flink.sepa;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;
import com.squareup.javapoet.TypeSpec;

import org.streampipes.model.base.ConsumableStreamPipesEntity;
import org.streampipes.model.staticproperty.StaticProperty;
import org.streampipes.wrapper.params.binding.EventProcessorBindingParams;
import org.streampipes.codegeneration.Generator;
import org.streampipes.codegeneration.utils.JFC;
import org.streampipes.codegeneration.utils.Utils;

public class ParametersGenerator extends Generator {

	public ParametersGenerator(ConsumableStreamPipesEntity element, String name, String packageName) {
		super(element, name, packageName);
	}

	public MethodSpec getConstructor() {
		Builder b = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
				.addParameter(JFC.SEPA_INVOCATION, "graph").addStatement("super(graph)");

		for (StaticProperty sp : element.getStaticProperties()) {
			String internalNameCamelCased = Utils.toCamelCase(sp.getInternalName());
			b.addParameter(JFC.STRING, internalNameCamelCased);
			b.addStatement("this.$N = $N", internalNameCamelCased, internalNameCamelCased);
		}

		return b.build();

	}
	

	@Override
	public JavaFile build() {
		MethodSpec constructor = getConstructor();

		TypeSpec.Builder parameterClass = TypeSpec.classBuilder(name + "Parameters").addModifiers(Modifier.PUBLIC)
				.superclass(EventProcessorBindingParams.class).addMethod(constructor);

		for (StaticProperty sp : element.getStaticProperties()) {
			String internalName = Utils.toCamelCase(sp.getInternalName());
			parameterClass.addField(JFC.STRING, internalName, Modifier.PRIVATE);
			MethodSpec getter = MethodSpec.methodBuilder(getterName(internalName)).addModifiers(Modifier.PUBLIC)
				.returns(JFC.STRING).addStatement("return " + internalName).build();
			parameterClass.addMethod(getter);
		}


		return JavaFile.builder(packageName, parameterClass.build()).build();
	}

	private String getterName(String s) {
		String result = s;
		if (s != null && s.length() > 0) {
			char first = Character.toUpperCase(s.charAt(0));
			result = "get" + first + s.substring(1);
		}
		
		return result;
	}

}
