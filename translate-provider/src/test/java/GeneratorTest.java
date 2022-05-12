import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.zzj.translate.Application;
import com.zzj.translate.model.entity.Means;
import com.zzj.translate.model.service.MeansService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GeneratorTest {
    @Autowired
    MeansService meansService;

    @Test
    public void a() {
        List<Means> list = meansService.list();
        System.out.println();
    }

    @Test
    public void generate() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/bbc_dict", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("zhouzhijun") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("/Users/xingchuan/gitCode/translate/translate-provider/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.zzj.translate") // 设置父包名
                            .moduleName("model") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "/Users/xingchuan/gitCode/translate/translate-provider/src/main/resources/com/zzj/translate/model/mapper"));
                })
                .strategyConfig(builder -> {
                    builder.addInclude("means") // 设置需要生成的表名
//                            .addTablePrefix("t_", "c_")
                    ; // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}