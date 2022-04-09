import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    // 关闭服务命令
    private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();

//防锁屏
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask(){
//            public void run(){
//                try
//                {
//                    Point p  = MouseInfo.getPointerInfo().getLocation();
//                    new Robot().mouseMove((int)p.getX (), (int)p.getY());
//                } catch (AWTException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }, 1,1000);
    }

    public void await() {
        ServerSocket serverSocket = null;
        int port = 16001;
        try {
            //服务器套接字对象
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // 循环等待一个请求
        while (true) {
            try {
                //等待连接，连接成功后，返回一个Socket对象
                Socket socket  = serverSocket.accept();
                InputStream input  = socket.getInputStream();
                OutputStream output = socket.getOutputStream();

                // 创建Request对象并解析
                Request request = new Request(input);
                request.parse();
                // 检查是否是关闭服务命令
                if (request.getUri().equals(SHUTDOWN_COMMAND)) {
                    break;
                }

                // 创建 Response 对象
                Response response = new Response(output);
                response.setRequest(request);
                response.sendStaticResource();

                // 关闭 socket 对象
                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}