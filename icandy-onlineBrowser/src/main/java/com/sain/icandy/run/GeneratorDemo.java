package com.sain.icandy.run;

import javax.sql.DataSource;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * GeneratorDemo
 */
public class GeneratorDemo {

	public static DataSource getDataSource() {
		Prop p = PropKit.use("application.properties");
		DruidPlugin druidDefault = new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p.get("password").trim(), p.get("driver"));
		druidDefault.start();
		return druidDefault.getDataSource();
	}

	public static void main(String[] args) {
		// base model 所使用的包名
		String baseModelPackageName = "com.gc.sdcmanager.model.base";
		// base model 文件保存路径
		String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/com/gc/sdcmanager/model/base";

		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = "com.gc.sdcmanager.model";
		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";

		// 创建生成器
		Generator gernerator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
		// 设置数据库方言
		gernerator.setDialect(new MysqlDialect());
		// 添加不需要生成的表名
		gernerator.addExcludedTable("adv");
		// 设置是否在 Model 中生成 dao 对象
		gernerator.setGenerateDaoInModel(true);
		// 设置是否生成字典文件
		gernerator.setGenerateDataDictionary(true);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为
		// "User"而非 OscUser
		gernerator.setRemovedTableNamePrefixes("t_");
		// 生成
		gernerator.generate();
	}
}
