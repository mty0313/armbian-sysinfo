package top.mty.controller.param;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ArmbianSysInfo {
  public static final String SYSTEM_LOAD = "systemLoad";
  public static final String UPTIME = "uptime";
  public static final String RAM = "ram";
  public static final String ZRAM = "zram";
  public static final String IP = "ip";
  public static final String CPU_TEMP = "cpuTemp";
  public static final String ROOT_USAGE = "rootUsage";

  private String systemLoad;
  private String uptime;
  private String ram;
  private String zram;
  private String ip;
  private String cpuTemp;
  private String rootUsage;
}
