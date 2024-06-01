public class JPrefixGeneratorStrategy extends DefaultGeneratorStrategy {
@Override
public String getJavaClassName(Definition definition, Mode mode) {
if (mode == Mode.DEFAULT) {
return "J" + super.getJavaClassName(definition, mode);
}
return super.getJavaClassName(definition, mode);
}
}
# Practice-jOOQ