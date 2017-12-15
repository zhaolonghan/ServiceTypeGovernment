package wancheng.com.servicetypegovernment.task;

/**
 * Created by john on 2017/12/15.
 */
public class  UserLocThread implements Runnable {
    //private
    public UserLocThread() {

    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (true) {
            try {
                Thread.sleep(10000);// 线程暂停10秒，单位毫秒
               /* Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);// 发送消息*/
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}