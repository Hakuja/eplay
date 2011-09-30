/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tags;

import groovy.lang.Closure;
import java.io.PrintWriter;
import java.util.Map;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;

/**
 *
 * @author tutumi
 */
@FastTags.Namespace("mytags")
public class MyFastTag extends FastTags {

    public static void _hello(Map<?, ?> args, Closure body, PrintWriter out,
            ExecutableTemplate template, int fromLine) {
        
        out.println("Hello " + args.get("name").toString() + " !");
    }
}
