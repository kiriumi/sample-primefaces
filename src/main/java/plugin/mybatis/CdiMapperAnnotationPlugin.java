package plugin.mybatis;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.IntrospectedTable.TargetRuntime;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.plugins.MapperAnnotationPlugin;

public class CdiMapperAnnotationPlugin extends MapperAnnotationPlugin {

    @Override
    public boolean clientGenerated(final Interface interfaze, final IntrospectedTable introspectedTable) {

        if (introspectedTable.getTargetRuntime() == TargetRuntime.MYBATIS3) {
            // don't need to do this for MYBATIS3_DSQL as that runtime already adds this annotation
            interfaze.addImportedType(
                    new FullyQualifiedJavaType("org.mybatis.cdi.Mapper")); //$NON-NLS-1$
            interfaze.addAnnotation("@Mapper"); //$NON-NLS-1$
        }
        return true;
    }
}
