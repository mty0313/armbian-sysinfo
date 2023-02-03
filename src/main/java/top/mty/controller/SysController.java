package top.mty.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mty.common.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/sys")
public class SysController {

  @GetMapping("/info")
  public R<List<String>> sysInfo() {
    List<String> commandResult = new ArrayList<>();
    try {
      List<String> commands = new ArrayList<>();
      commands.add("cd src/main/resources/scripts/");
      commands.add("./armbian-sysinfo");
      commandResult = execute(commands);
      return R.success(commandResult);
    } catch (Exception e) {
      return R.error(e.getMessage());
    }
  }


  public List<String> execute(List<String> commands) throws IOException, InterruptedException {
    Runtime run = Runtime.getRuntime();
    Process proc = run.exec("/bin/bash", null, null);
    BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
    for (String line : commands) {
      out.println(line);
    }
    out.println("exit");
    String rspLine = "";
    List<String> result = new ArrayList<>();
    while ((rspLine = in.readLine()) != null) {
      result.add(rspLine);
    }
    proc.waitFor();
    in.close();
    out.close();
    proc.destroy();
    return result;
  }

}
