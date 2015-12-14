package com.srt.appguard.dex;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jf.dexlib.AnnotationDirectoryItem;
import org.jf.dexlib.AnnotationDirectoryItem.FieldAnnotation;
import org.jf.dexlib.AnnotationDirectoryItem.FieldAnnotationIteratorDelegate;
import org.jf.dexlib.AnnotationDirectoryItem.MethodAnnotation;
import org.jf.dexlib.AnnotationDirectoryItem.MethodAnnotationIteratorDelegate;
import org.jf.dexlib.AnnotationDirectoryItem.ParameterAnnotation;
import org.jf.dexlib.AnnotationDirectoryItem.ParameterAnnotationIteratorDelegate;
import org.jf.dexlib.AnnotationItem;
import org.jf.dexlib.AnnotationSetItem;
import org.jf.dexlib.AnnotationSetRefList;
import org.jf.dexlib.ClassDataItem;
import org.jf.dexlib.ClassDataItem.EncodedField;
import org.jf.dexlib.ClassDataItem.EncodedMethod;
import org.jf.dexlib.ClassDefItem;
import org.jf.dexlib.ClassDefItem.StaticFieldInitializer;
import org.jf.dexlib.CodeItem;
import org.jf.dexlib.CodeItem.EncodedCatchHandler;
import org.jf.dexlib.CodeItem.EncodedTypeAddrPair;
import org.jf.dexlib.CodeItem.TryItem;
import org.jf.dexlib.DexFile;
import org.jf.dexlib.FieldIdItem;
import org.jf.dexlib.Item;
import org.jf.dexlib.MethodIdItem;
import org.jf.dexlib.ProtoIdItem;
import org.jf.dexlib.StringDataItem;
import org.jf.dexlib.StringIdItem;
import org.jf.dexlib.TypeIdItem;
import org.jf.dexlib.TypeListItem;
import org.jf.dexlib.Code.Instruction;
import org.jf.dexlib.Code.InstructionWithReference;
import org.jf.dexlib.EncodedValue.AnnotationEncodedSubValue;
import org.jf.dexlib.EncodedValue.AnnotationEncodedValue;
import org.jf.dexlib.EncodedValue.ArrayEncodedSubValue;
import org.jf.dexlib.EncodedValue.ArrayEncodedValue;
import org.jf.dexlib.EncodedValue.BooleanEncodedValue;
import org.jf.dexlib.EncodedValue.ByteEncodedValue;
import org.jf.dexlib.EncodedValue.CharEncodedValue;
import org.jf.dexlib.EncodedValue.DoubleEncodedValue;
import org.jf.dexlib.EncodedValue.EncodedValue;
import org.jf.dexlib.EncodedValue.EnumEncodedValue;
import org.jf.dexlib.EncodedValue.FieldEncodedValue;
import org.jf.dexlib.EncodedValue.FloatEncodedValue;
import org.jf.dexlib.EncodedValue.IntEncodedValue;
import org.jf.dexlib.EncodedValue.LongEncodedValue;
import org.jf.dexlib.EncodedValue.MethodEncodedValue;
import org.jf.dexlib.EncodedValue.NullEncodedValue;
import org.jf.dexlib.EncodedValue.ShortEncodedValue;
import org.jf.dexlib.EncodedValue.StringEncodedValue;
import org.jf.dexlib.EncodedValue.TypeEncodedValue;


public class DexMerger {

	protected DexFile target;
	protected DexFile source;
	
	public DexMerger(final File target) throws IOException {
		this(new DexFile(target));
	}
	
	public DexMerger(final DexFile target) {
		this.target = target;
	}

	public void merge(final File source) throws IOException {
		merge(new DexFile(source), null);
	}
	
//	public void merge(final File source, final Collection<String> classes) throws IOException {
//		merge(new DexFile(source), classes);
//	}

//	public void merge(final DexFile source) {
//		merge(source, null);
//	}

	private void merge(final DexFile source, final Collection<String> classes) {
		this.source = source;

		// merge all class-defs recursively
		for (final ClassDefItem item : source.ClassDefsSection.getItems()) {
			if (classes == null || classes.contains(item.getClassType().getTypeDescriptor())) {
				merge(item);
			}
		}
	}
	
	
	protected ClassDefItem merge(final ClassDefItem item) {
		// TODO merge staticFieldInitializers!
		final List<StaticFieldInitializer> staticFieldInitializers = new LinkedList<StaticFieldInitializer>();

		return ClassDefItem.internClassDefItem(target,
				merge(item.getClassType()),
				item.getAccessFlags(),
				merge(item.getSuperclass()),
				merge(item.getInterfaces()),
				merge(item.getSourceFile()),
				merge(item.getAnnotations()),
				merge(item.getClassData()),
				staticFieldInitializers);
	}
	
	protected TypeIdItem merge(final TypeIdItem item) {
		return TypeIdItem.internTypeIdItem(target, item.getTypeDescriptor());
	}
	
	protected TypeListItem merge(final TypeListItem item) {
		if (item == null) {
			return null;
		}

		final List<TypeIdItem> types = new LinkedList<TypeIdItem>();
		for (final TypeIdItem type : item.getTypes()) {
			types.add(merge(type));
		}

		return TypeListItem.internTypeListItem(target, types);
	}
	
	protected StringIdItem merge(final StringIdItem item) {
		if (item == null) {
			return null;
		}

		return StringIdItem.internStringIdItem(target, item.getStringValue());
	}
	
	protected StringDataItem merge(final StringDataItem item) {
		return StringDataItem.internStringDataItem(target, item.getStringValue());
	}
	
	protected ClassDataItem merge(final ClassDataItem item) {
		final List<EncodedField> staticFields = new LinkedList<EncodedField>();
		for (final EncodedField field : item.getStaticFields()) {
			staticFields.add(new EncodedField(
					merge(field.field),
					field.accessFlags
			));
		}
		
		final List<EncodedField> instanceFields = new LinkedList<EncodedField>();
		for (final EncodedField field : item.getInstanceFields()) {
			instanceFields.add(new EncodedField(
					merge(field.field),
					field.accessFlags
			));
		}
		
		final List<EncodedMethod> directMethods = new LinkedList<EncodedMethod>();
		for (final EncodedMethod method : item.getDirectMethods()) {
			directMethods.add(new EncodedMethod(
					merge(method.method),
					method.accessFlags,
					merge(method.codeItem)
			));
		}
		
		final List<EncodedMethod> virtualMethods = new LinkedList<EncodedMethod>();
		for (final EncodedMethod method : item.getVirtualMethods()) {
			virtualMethods.add(new EncodedMethod(
					merge(method.method),
					method.accessFlags,
					merge(method.codeItem)
			));
		}

		return ClassDataItem.internClassDataItem(target,
				staticFields,
				instanceFields,
				directMethods,
				virtualMethods);
	}
	
	protected FieldIdItem merge(final FieldIdItem item) {
		return FieldIdItem.internFieldIdItem(target,
				merge(item.getContainingClass()),
				merge(item.getFieldType()),
				merge(item.getFieldName()));
	}
	
	protected MethodIdItem merge(final MethodIdItem item) {
		return MethodIdItem.internMethodIdItem(target,
				merge(item.getContainingClass()),
				merge(item.getPrototype()),
				merge(item.getMethodName()));
	}
	
	protected ProtoIdItem merge(final ProtoIdItem item) {
		return ProtoIdItem.internProtoIdItem(target,
				merge(item.getReturnType()),
				merge(item.getParameters()));
	}

	protected CodeItem merge(final CodeItem item) {
		if (item == null) {
			return null;
		}

		// fix references in instructions
		final List<Instruction> instructions = new LinkedList<Instruction>();
		for (final Instruction instruction : item.getInstructions()) {
			if (instruction instanceof InstructionWithReference) {
				final InstructionWithReference iwr = (InstructionWithReference) instruction;
				final Item<?> referencedItem;

				switch (iwr.getReferenceType()) {
					case field:
						referencedItem = merge((FieldIdItem) iwr.getReferencedItem());
						break;
					case method:
						referencedItem = merge((MethodIdItem) iwr.getReferencedItem());
						break;
					case string:
						referencedItem = merge((StringIdItem) iwr.getReferencedItem());
						break;
					case type:
						referencedItem = merge((TypeIdItem) iwr.getReferencedItem());
						break;
					default:
						referencedItem = null;
				}

				iwr.setReferencedItem(referencedItem);
			}
			
			instructions.add(instruction);
		}

		// fix tries and catch handlers
		List<EncodedCatchHandler> handlers = null;
		List<TryItem> tries = null;

		// first update type refs in catch handlers and
		// create a map from old to new handlers
		final Map<EncodedCatchHandler, EncodedCatchHandler> handlerMap = new HashMap<EncodedCatchHandler, EncodedCatchHandler>();
		final EncodedCatchHandler[] itemHandlers = item.getHandlers();
		if (itemHandlers != null) {
			handlers = new LinkedList<EncodedCatchHandler>();
			for (final EncodedCatchHandler handler : itemHandlers) {
				final EncodedTypeAddrPair[] pairs = new EncodedTypeAddrPair[handler.handlers.length];
				for (int i = 0; i < pairs.length; ++i) {
					final EncodedTypeAddrPair p = handler.handlers[i];
					pairs[i] = new EncodedTypeAddrPair(merge(p.exceptionType), p.getHandlerAddress());
				}
				
				final EncodedCatchHandler newHandler = new EncodedCatchHandler(pairs, handler.getCatchAllHandlerAddress());
				handlerMap.put(handler, newHandler);
				handlers.add(newHandler);
			}
		}
		
		// then update the references to the catch handlers in the try items
		final TryItem[] itemTries = item.getTries();
		if (itemTries != null) {
			tries = new LinkedList<TryItem>();
			for (final TryItem tri : itemTries) {
				tries.add(new TryItem(
						tri.getStartCodeAddress(),
						tri.getTryLength(),
						handlerMap.get(tri.encodedCatchHandler)
				));
			}
		}

		return CodeItem.internCodeItem(target,
				item.getRegisterCount(),
				item.getInWords(),
				item.getOutWords(),
				null, // merge(item.getDebugInfo()), we don't need the debug info
				instructions,
				tries,
				handlers);
	}

	/*
	protected DebugInfoItem merge(final DebugInfoItem item) {
		StringIdItem[] parameterNames = item.getParameterNames();
		for (int i = 0; i < parameterNames.length; ++i) {
			parameterNames[i] = merge(parameterNames[i]);
		}
		
		return DebugInfoItem.internDebugInfoItem(target,
				item.getLineStart(),
				parameterNames,
				item.getEncodedDebugInfo(),
				item.getReferencedItems()); // TODO check if we need to merge the referenced items explicitly
	}
	*/

	protected AnnotationDirectoryItem merge(final AnnotationDirectoryItem item) {
		if (item == null) {
			return null;
		}

		final List<FieldAnnotation> fields = new LinkedList<FieldAnnotation>();
		item.iterateFieldAnnotations(new FieldAnnotationIteratorDelegate() {
			@Override
			public void processFieldAnnotations(FieldIdItem field, AnnotationSetItem annotationSet) {
				fields.add(new FieldAnnotation(
						merge(field),
						merge(annotationSet)
				));
			}
		});

		final List<MethodAnnotation> methods = new LinkedList<MethodAnnotation>();
		item.iterateMethodAnnotations(new MethodAnnotationIteratorDelegate() {
			@Override
			public void processMethodAnnotations(MethodIdItem method, AnnotationSetItem annotationSet) {
				methods.add(new MethodAnnotation(
						merge(method),
						merge(annotationSet)
				));
			}
		});

		final List<ParameterAnnotation> parameters = new LinkedList<ParameterAnnotation>();
		item.iterateParameterAnnotations(new ParameterAnnotationIteratorDelegate() {
			@Override
			public void processParameterAnnotations(MethodIdItem method, AnnotationSetRefList annotationSet) {
				parameters.add(new ParameterAnnotation(
						merge(method),
						merge(annotationSet)
				));
			}
		});

		return AnnotationDirectoryItem.internAnnotationDirectoryItem(target,
				merge(item.getClassAnnotations()),
				fields,
				methods,
				parameters);
	}
	
	protected AnnotationSetItem merge(final AnnotationSetItem item) {
		if (item == null) {
			return null;
		}

		final List<AnnotationItem> annotations = new LinkedList<AnnotationItem>();
		for (final AnnotationItem annotation : item.getAnnotations()) {
			annotations.add(merge(annotation));
		}

		return AnnotationSetItem.internAnnotationSetItem(target, annotations);
	}

	protected AnnotationSetRefList merge(final AnnotationSetRefList item) {
		if (item.getAnnotationSets().length == 0) {
			return null;
		}

		final List<AnnotationSetItem> annotationSets = new LinkedList<AnnotationSetItem>();
		for (final AnnotationSetItem annotationSet : item.getAnnotationSets()) {
			annotationSets.add(merge(annotationSet));
		}

		return AnnotationSetRefList.internAnnotationSetRefList(target, annotationSets);
	}

	protected AnnotationItem merge(final AnnotationItem item) {
		return AnnotationItem.internAnnotationItem(target,
				item.getVisibility(),
				//item.getEncodedAnnotation());
				merge(item.getEncodedAnnotation()));
	}
	
	protected AnnotationEncodedSubValue merge(final AnnotationEncodedSubValue item) {
		final int numValues = item.names.length;
		final StringIdItem[] names = new StringIdItem[numValues];
		final EncodedValue[] values = new EncodedValue[numValues];
		
		for (int i = 0; i < numValues; ++i) {
			final StringIdItem name = item.names[i];
			final EncodedValue value = item.values[i];
			names[i] = merge(name);
			values[i] = merge(value);
		}
		
		return new AnnotationEncodedSubValue(
				merge(item.annotationType),
				names,
				values);
	}

	protected ArrayEncodedSubValue merge(final ArrayEncodedSubValue item) {
		final int numValues = item.values.length;
		final EncodedValue[] values = new EncodedValue[numValues];
		
		for (int i = 0; i < numValues; ++i) {
			values[i] = merge(item.values[i]);
		}

		return new ArrayEncodedSubValue(values);
	}

	protected EncodedValue merge(final EncodedValue item) {
		switch (item.getValueType()) {
			case VALUE_FIELD:
			{
				final FieldEncodedValue val = (FieldEncodedValue) item;
				return new FieldEncodedValue(merge(val.value));
			}
			case VALUE_METHOD:
			{
				final MethodEncodedValue val = (MethodEncodedValue) item;
				return new MethodEncodedValue(merge(val.value));
			}
			case VALUE_STRING:
			{
				final StringEncodedValue val = (StringEncodedValue) item;
				return new StringEncodedValue(merge(val.value));
			}
			case VALUE_TYPE:
			{
				final TypeEncodedValue val = (TypeEncodedValue) item;
				return new TypeEncodedValue(merge(val.value));
			}
			case VALUE_ENUM:
			{
				final EnumEncodedValue val = (EnumEncodedValue) item;
				return new EnumEncodedValue(merge(val.value));
			}
			case VALUE_ANNOTATION:
			{
				final AnnotationEncodedSubValue val = (AnnotationEncodedSubValue) item;
				final int numValues = val.names.length;
				final StringIdItem[] names = new StringIdItem[numValues];
				final EncodedValue[] values = new EncodedValue[numValues];
				
				for (int i = 0; i < numValues; ++i) {
					final StringIdItem name = val.names[i];
					final EncodedValue value = val.values[i];
					names[i] = merge(name);
					values[i] = merge(value);
				}
				
				return new AnnotationEncodedValue(
						merge(val.annotationType),
						names,
						values);
			}
			case VALUE_ARRAY:
			{
				final ArrayEncodedSubValue val = (ArrayEncodedSubValue) item;
				final int numValues = val.values.length;
				final EncodedValue[] values = new EncodedValue[numValues];
				
				for (int i = 0; i < numValues; ++i) {
					values[i] = merge(val.values[i]);
				}

				return new ArrayEncodedValue(values);
			}
			
			// primitives: no merge required, we just clone them
			// TODO maybe we can omit this
			case VALUE_BOOLEAN:
			{
				final BooleanEncodedValue val = (BooleanEncodedValue) item;
				return BooleanEncodedValue.getBooleanEncodedValue(val.value);
			}
			case VALUE_BYTE:
			{
				final ByteEncodedValue val = (ByteEncodedValue) item;
				return new ByteEncodedValue(val.value);
			}
			case VALUE_CHAR:
			{
				final CharEncodedValue val = (CharEncodedValue) item;
				return new CharEncodedValue(val.value);
			}
			case VALUE_DOUBLE:
			{
				final DoubleEncodedValue val = (DoubleEncodedValue) item;
				return new DoubleEncodedValue(val.value);
			}
			case VALUE_FLOAT:
			{
				final FloatEncodedValue val = (FloatEncodedValue) item;
				return new FloatEncodedValue(val.value);
			}
			case VALUE_INT:
			{
				final IntEncodedValue val = (IntEncodedValue) item;
				return new IntEncodedValue(val.value);
			}
			case VALUE_LONG:
			{
				final LongEncodedValue val = (LongEncodedValue) item;
				return new LongEncodedValue(val.value);
			}
			case VALUE_NULL:
			{
				return NullEncodedValue.NullValue;
			}
			case VALUE_SHORT:
			{
				final ShortEncodedValue val = (ShortEncodedValue) item;
				return new ShortEncodedValue(val.value);
			}
		}
		return null;
	}

	protected static <T> List<T> toList(T ... a) {
		if (a == null) {
			return null;
		}
		return Arrays.asList(a);
	}
}
