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
                Thread.sleep(10000);// �߳���ͣ10�룬��λ����
               /* Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);// ������Ϣ*/
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}