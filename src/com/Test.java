package com;

import dao.UserImpl;
import mysql.DatabaseImpl;
import net.Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static net.Lei.csz;

/**
 * @author setusb
 * @version 1.0
 * @date 2021/1/23 18:01
 */
public class Test {
    //定义全局金币初始化
    int[] money = {0};
    private static void dh(int[] money, Frame shop, JButton shopjb11) {
        shopjb11.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int opesn = JOptionPane.showConfirmDialog(shop, "需要金币: 3000\n兑换后只能查看一次，关闭后需重新兑换");
                if (JOptionPane.OK_OPTION == opesn) {
                    if (money[0] >= 3000) {
                        money[0] -= 3000;
                        JOptionPane.showMessageDialog(shop, "嘿嘿嘿！\n目前还没有更新呢！");
                    } else {
                        JOptionPane.showMessageDialog(shop, "你没有足够的金币来兑换");
                    }
                } else {
                    JOptionPane.showMessageDialog(shop, "你取消了兑换");
                }
            }
        });
    }

    public static void naihuan(int[] money, Frame shop, JButton shopjb1, JButton shopjb2, JButton shopjb3, JButton shopjb4, JButton shopjb5) {
        dh(money, shop, shopjb1);
        dh(money, shop, shopjb2);
        dh(money, shop, shopjb3);
        dh(money, shop, shopjb4);
        dh(money, shop, shopjb5);
    }

    boolean debug = true;

    {
        InputStream in = Login.class.getClassLoader().getResourceAsStream("config.properties");
        Properties prop = new Properties();
        try {
            prop.load(in);
            String a1 = prop.getProperty("tio");
            String a2 = prop.getProperty("debug");
            debug = Boolean.parseBoolean(a2);
        } catch (IOException e) {
            System.out.println("读取config.properties出现未知错误，请联系开发者！");
        }
    }

    public void test(String name,String password,int moneys) {
        money[0] += moneys;
        Frame frame = new Frame("BombGame - 数字炸弹游戏");
        JFrame jf0 = new JFrame("游戏程序");
        JFrame shoT = new JFrame("游戏商店");
        Frame jf1=(Frame)jf0;
        Frame shop=(Frame)shoT;

        //设置窗口图标
        /*frame.setIconImage(new ImageIcon("com/icon.png").getImage());*/
        /*
         * https://blog.csdn.net/shijiebei2009/article/details/25305439
         * 解决在idea中JFrame自定义图标可以显示，但是导出JAR之后无法显示
         * */
        String imagePath = "icon.png";
        Image imageIcon = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource(imagePath));
        Image imageIcons = Toolkit.getDefaultToolkit().getImage(imagePath);
        frame.setIconImage(imageIcon);
        frame.setVisible(true);
        jf1.setIconImage(imageIcon);
        jf1.setVisible(false);
        shop.setIconImage(imageIcon);
        shop.setVisible(false);
        /**
         * 无法修复商店和游戏界面的图标显示问题，历史遗留bug
         * */
/*        jf1.setIconImage(imageIcons);
        shop.setIconImage(imageIcons);*/

        //设置窗口大小和位置
        frame.setSize(525, 235);
        frame.setResizable(false);
        //创建一个按钮
/*        JButton jb5 = new JButton("登录");*/
        JButton jb1 = new JButton("游戏");
        JButton jb2 = new JButton("商店");
        JButton jb3 = new JButton("特权码");
        JButton jb4 = new JButton("更多");
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp3 = new JPanel();
        JPanel jp4 = new JPanel();
/*        JPanel jp5 = new JPanel();*/
        jp1.add(jb1);
        jp2.add(jb2);
        jp3.add(jb3);
        jp4.add(jb4);
/*        jp5.add(jb5);*/
/*        frame.add(jp5);*/
        frame.add(jp1);
        frame.add(jp2);
        frame.add(jp3);
        frame.add(jp4);
        jb1.setFont(new Font("微软雅黑", Font.BOLD, 12));
        jb2.setFont(new Font("微软雅黑", Font.BOLD, 12));
        jb3.setFont(new Font("微软雅黑", Font.BOLD, 12));
        jb4.setFont(new Font("微软雅黑", Font.BOLD, 12));

        if (debug) {
            System.out.println("用户："+name+"\n"+"密码："+password+"\n"+"金钱："+moneys);
        }

/*        jb5.setFont(new Font("微软雅黑", Font.BOLD, 12));*/
        //创建文本框
        JTextArea jtf = new JTextArea("\n欢迎您 <"+name+"> 游戏规则介绍:\n" +
                "猜数字，数字会不断缩小，直到你猜到正确的数字\n" +
                "倘若你猜错次数达到5次以上，你获得金币的收益会减半\n" +
                "如果你猜对，你将会获得金币奖励\n" +
                "\n金币的具体用途可以解锁商店的物品\n" +
                "也可以帮助你获取退出的密码" +
                "\t\t     DB.MACT.TOP", 10, 38);
        frame.setLocationRelativeTo(null);
        frame.add(jtf);
        jtf.setEditable(false);
        //查看文本框可见范围
        //jtf.setBackground(new Color(244,244,244));
        //设置布局管理器
        frame.setLayout(new FlowLayout());
        frame.addWindowListener(new WindowAdapter() {
            //存储判断输入错误的数据
            final int math = 0;

            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(frame, "是否退出本软件?");
                if (JOptionPane.OK_OPTION == option) {
                    UserImpl user = new UserImpl();
                    /*System.out.println("返回值: "+user.moneyModify(name, password, money[0]));*/
                    if (user.moneyModify(name, password, money[0]) > 0) {
                        if (debug) {
                            System.out.println("Debug - 已提交信息");
                        }
                    } else {
                        if (debug) {
                            System.out.println("Debug - 提交信息失败");
                        }
                        JOptionPane.showMessageDialog(frame,"金钱提交服务器失败，请截此窗口联系开发者!\n用户: "+name+"\t金钱: "+money[0] );
                    }
                    System.exit(0);
                }
            }
        });
        JTextArea jtf1 = new JTextArea("\t         【历史记录】\n", 6, 30);
        jtf1.setVisible(true);
        jtf1.setEditable(false);
        jf1.add(jtf1);

        Font font = jtf1.getFont();
        jtf1.setFont(new Font(font.getName(), font.getStyle(), 20));
        jtf1.setBackground(new Color(238, 238, 238));
        JPanel jp1_1 = new JPanel();
        JButton jb1_1 = new JButton("请点击");
        jb1_1.setLocation(10, 10);
        jb1.addMouseListener(new MouseAdapter() {
            boolean open = true;
            //定义数值范围
            int fw = 100;
            int fws = 0;
            int a = 0;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (open) {
                    jf1.setVisible(true);
                    jf1.setSize(624, 275);
                    jf1.setLocationRelativeTo(null);
                    jf1.setResizable(false);
                    Dimension preferredSize = new Dimension(300, 100);
                    jb1_1.setPreferredSize(preferredSize);
                    jb1_1.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            String answer = JOptionPane.showInputDialog(jf1, "请输入你想猜的数字\n当前范围(小于)" + fw + "\n每一次错误，值都会减20");
                            if ("".equals(answer)) {
                                JOptionPane.showMessageDialog(jf1, "你并没有输入任何值");
                            } else {
                                int[] arr = new int[0];
                                try {
                                    arr = csz(answer, fw);
                                    fw = arr[0];
                                    fws = arr[1];
                                    money[0] += arr[2];
                                } catch (Exception exception) {
                                    if (debug) {
                                        System.out.println("Debug - 关闭猜数字框");
                                    }
                                }
                                if (fws != 0) {
                                    jtf1.append(fws + "   ");
                                    a++;
                                    if (a == 12) {
                                        jtf1.append("\n");
                                        a = 0;
                                    }
                                }
                            }
                        }
                    });
                    jf1.add(jp1_1);
                    jp1_1.add(jb1_1);
                    jb1_1.setPreferredSize(new Dimension(133, 33));
                    //设置窗口图标
                    jf1.setIconImage(
                            Toolkit.getDefaultToolkit().getImage("icon.png")
                    );
                }
                open = false;
                jf1.setLayout(new FlowLayout());
                jf1.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        open = true;
                    }
                });
            }
        });
        JPanel shopjp1 = new JPanel();
        JTextArea shopjt1 = new JTextArea("\n\n\t            你当前的金币是: " + money[0], 5, 36);
        shopjt1.setBackground(new Color(255, 255, 255));
        Font fonts = shopjt1.getFont();
        shopjt1.setEditable(false);
        shopjt1.setFont(new Font(fonts.getName(), fonts.getStyle(), 20));
        //兑换按钮
        JButton shopjb1 = new JButton("未更新");
        JButton shopjb2 = new JButton("未更新");
        JButton shopjb3 = new JButton("未更新");
        JButton shopjb4 = new JButton("未更新");
        JButton shopjb5 = new JButton("未更新");
        JButton shopjb6 = new JButton("未更新");
        JButton shopjb7 = new JButton("未更新");
        JButton shopjb8 = new JButton("未更新");
        JButton shopjb9 = new JButton("未更新");
        JButton shopjb10 = new JButton("未更新");
        JButton shopjb11 = new JButton("未更新");
        JButton shopjb12 = new JButton("退出密码");
/*        jb5.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Login login1 = new Login();
                login1.logins();
            }
        });*/
        jb2.addMouseListener(new MouseAdapter() {
            boolean open = true;
            @Override
            public void mouseClicked(MouseEvent e) {
                if (open) {
                    shop.setVisible(true);
                    shop.setSize(624, 275);
                    shop.setLocationRelativeTo(null);
                    shop.setResizable(false);
                    shopjp1.add(shopjt1);
                    shop.add(shopjp1);
                    //shopjt1.setText("金钱是 " + money[0]);
                    //使用ScheduledExecutorService代替Timer
                    //timer单线程，性能较低，容易卡顿
                    //优化！！！
                    ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
/*                    Timer shoptime = new Timer(true);
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            shopjt1.setText("\n\n\t            你当前的金币是: " + money[0]);
                        }
                    };*/
                    /*shoptime.schedule(task, 1000, 1000);*/
                    service.scheduleAtFixedRate(() -> shopjt1.setText("\n\n\t            你当前的金币是: " + money[0]), 1, 1, TimeUnit.SECONDS);
                    /*
                     * 兑换按钮:
                     * 优化循环算法，大幅度节省代码量
                     */
                    for (JButton jButton : Arrays.asList(shopjb1, shopjb2, shopjb3, shopjb4, shopjb5, shopjb6, shopjb7, shopjb8, shopjb9, shopjb10, shopjb11, shopjb12)) {
                        shop.add(jButton);
                    }

                    //设置窗口图标
                    shop.setIconImage(
                            Toolkit.getDefaultToolkit().getImage("icon.png")
                    );
                }
                open = false;
                shop.setLayout(new FlowLayout());
                shop.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        open = true;
                    }
                });
            }
        });
        // * 商店内容
        // * 优化循环算法，大幅节省代码量
        naihuan(money, shop, shopjb1, shopjb2, shopjb3, shopjb4, shopjb5);
        naihuan(money, shop, shopjb6, shopjb7, shopjb8, shopjb9, shopjb10);
        dh(money, shop, shopjb11);
        shopjb12.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int opesn = JOptionPane.showConfirmDialog(shop, "需要金币: 500\n兑换后只能查看一次，关闭后需重新兑换");
                if (JOptionPane.OK_OPTION == opesn) {
                    if (money[0] >= 500) {
                        money[0] -= 500;
                        JOptionPane.showMessageDialog(shop, "safqhbfjt325472342342");
                    } else {
                        JOptionPane.showMessageDialog(shop, "你没有足够的金币来兑换");
                    }
                    /*System.out.println(money[0]);*/
                } else {
                    JOptionPane.showMessageDialog(shop, "你取消了兑换");
                }
            }
        });
        jb3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ArrayList<String> code = new ArrayList<>();
                DatabaseImpl database = new DatabaseImpl();
                database.linkDatabase();
                ResultSet rs = database.inquiryDatabase("select * from ActivationCode where verification = 0");
                try {
                    while (rs.next()) {
                        code.add(rs.getString("code"));
                    }
                } catch (SQLException a) {
                    System.out.println("出错");
                }
                if (debug) {
                    System.out.println("DeBug - 打开了特权码");
                }
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "客户端无法访问特权码\n此功能已被禁用");
                } else {
                    try {
                        boolean flas = true;
                        String sz = JOptionPane.showInputDialog(frame, "请输入特权码\n(使用特权码可以迅速获得大量金币)\n(购买特权码请联系软件发布者)");
                        for (String a : code)
                        {
                            if (sz.equals(a)) {
                                money[0] += 1000;
                                flas = false;
                                database.modifyDatabase("update activationcode set verification = '1' where code = '" + a + "'");
                                JOptionPane.showMessageDialog(frame, "兑换码："+a+"\n兑换成功");
                                break;
                            }
                        }
                        if (flas) {
                            JOptionPane.showMessageDialog(frame, "你输入的特权码不对");
                        }
                    } catch (Exception exception) {
                        if (debug) {
                            System.out.println("DeBug - 关闭了特权码");
                        }
                    }
                }
                database.closeDatabase();
            }
        });
        jb4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(frame, "软件制作人: 吊毛猫" +
                        "\n软件当前版本: v210330 正式运行版" +
                        "\n发布网站: github.com/setusb/Bomb");
            }
        });
    }
}
