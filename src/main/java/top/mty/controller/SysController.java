package top.mty.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.mty.common.R;
import top.mty.controller.param.ArmbianSysInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sys")
@Slf4j
public class SysController {

  @GetMapping("/info")
  public R<ArmbianSysInfo> sysInfo() {
    List<String> commandResult;
    try {
      List<String> commands = new ArrayList<>();
      commands.add("cd ~/scripts");
      commands.add("./armbian-sysinfo");
      commandResult = execute(commands);
      log.debug(arrayToString(commandResult));
      ArmbianSysInfo sysInfo = new ArmbianSysInfo();
      for (String r : commandResult) {
        String[] split = r.split(":");
        if (split.length < 2) {
          continue;
        }
        String key = r.split(":")[0];
        String value = r.split(":")[1];
        if (noUsefulValue(value)) {
          continue;
        }
        if (StringUtils.hasText(key) && StringUtils.hasText(value)) {
          switch (key) {
            case ArmbianSysInfo.SYSTEM_LOAD:
              sysInfo.setSystemLoad(value);
              break;
            case ArmbianSysInfo.IP:
              sysInfo.setIp(value);
              break;
            case ArmbianSysInfo.CPU_TEMP:
              sysInfo.setCpuTemp(value);
              break;
            case ArmbianSysInfo.RAM:
              sysInfo.setRam(value);
              break;
            case ArmbianSysInfo.ZRAM:
              sysInfo.setZram(value);
              break;
            case ArmbianSysInfo.ROOT_USAGE:
              sysInfo.setRootUsage(value);
              break;
            case ArmbianSysInfo.UPTIME:
              sysInfo.setUptime(value + " hours");
              break;
          }
        }
      }
      return R.success(sysInfo);
    } catch (Exception e) {
      return R.error(e.toString());
    }
  }

  /**
   * 判断是否无值
   * @param value 值
   * @return bool
   */
  public boolean noUsefulValue(String value) {
    if (!StringUtils.hasText(value)) {
      return true;
    }
    return value.equals("°C") || value.equals("%");
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

  public String arrayToString(List<String> array) {
    if (CollectionUtils.isEmpty(array)) {
      return "Empty Array";
    }
    StringBuilder r = new StringBuilder();
    for (String s : array) {
      r.append(s).append(";");
    }
    return r.toString();
  }

}
