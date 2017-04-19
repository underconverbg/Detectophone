package com.underconverbg.detectophone;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2017/4/19.
 */

public class MyRecorder2
{
    private static int RECORD_RATE = 0;
    private static int RECORD_BPP = 32;
    private static int RECORD_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    private static int RECORD_ENCODER = AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord audioRecorder = null;
    private Boolean isRecording = false;
    private int bufferEle = 1024, bytesPerEle = 2;// want to play 2048 (2K) since 2 bytes we use only 1024 2 bytes in 16bit format
    private static int[] recordRate ={44100 , 22050 , 11025 , 8000};
    int bufferSize = 0;
    File uploadFile;


    private String phoneNumber;
    private boolean isCommingNumber = false;//是否是来电
    private String date;

    public void startRecord(){
        audioRecorder = initializeRecord();
        if (audioRecorder != null){
            Log.e("MyRecorder2", "startRecord :"+":录音");
            audioRecorder.startRecording();
            Log.e("MyRecorder2", "startRecord :"+":录音2");
        }else
            return;

        isRecording = true;

        writeToFile();

    }

    /*
       * Initialize audio record
       *
       * @param
       * @return android.media.AudioRecord
       */
/*
     * Initialize audio record
     *
     * @param
     * @return android.media.AudioRecord
     */
    private AudioRecord initializeRecord()
    {
        short[] audioFormat = new short[]{AudioFormat.ENCODING_PCM_16BIT, AudioFormat.ENCODING_PCM_8BIT};
        short[] channelConfiguration = new short[]{AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO};
        for (int rate : recordRate)
        {
            for (short aFormat : audioFormat) {
                for (short cConf : channelConfiguration) {
                    //Log.d("MainActivity:initializeRecord()","Rate"+rate+"AudioFormat"+aFormat+"Channel Configuration"+cConf);
                    try {
                        int buffSize = AudioRecord.getMinBufferSize(rate, cConf, aFormat);
                        bufferSize = buffSize;

                        if (buffSize != AudioRecord.ERROR_BAD_VALUE) {
                            AudioRecord aRecorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, cConf, aFormat, buffSize);

                            if (aRecorder.getState() == AudioRecord.STATE_INITIALIZED) {
                                RECORD_RATE = rate;
                                //Log.d("MainActivity:InitializeRecord - AudioFormat",String.valueOf(aFormat));
                                //Log.d("MainActivity:InitializeRecord - Channel",String.valueOf(cConf));
                                //Log.d("MainActivity:InitialoizeRecord - rceordRate", String.valueOf(rate));
                                return aRecorder;
                            }
                        }
                    } catch (Exception e) {
                        //Log.e("MainActivity:initializeRecord()",e.getMessage());
                    }
                }
            }
        }
        return null;
    }

    /*
   * Method to stop and release audio record
   *
   * @param
   * @return void
   */
    public void stopRecord()
    {
        if (null != audioRecorder)
        {
            Log.e("MyRecorder2", "stopRecord :"+":录音");
            isRecording = false;
            audioRecorder.stop();
            audioRecorder.release();
            audioRecorder = null;

            convertRawToWavFile(createTempFile(), createWavFile());
            if (uploadFile.exists()) {
                //Log.d("AudioRecorderService:stopRecord()", "UploadFile exists");
            }
            deletTempFile();
        }

    }

    //Creates temporary .raw file for recording
    private File createTempFile()
    {
        File recordPath = new File(Environment.getExternalStorageDirectory()
                , "/mydetectophone");
        if (!recordPath.exists()) {
            recordPath.mkdirs();
            Log.e("recorder", "创建目录");
        }
        else if( !recordPath.isDirectory() && recordPath.canWrite() )
        {
            Log.e("recorder", "删除创建目录");
            recordPath.delete();
            recordPath.mkdirs();
        }
        else
        {
            //you can't access there with write permission.
            //Try other way.
        }

        String callDir = "out";
        if (isCommingNumber)
        {
            callDir = "in";
        }


        date =  new SimpleDateFormat("yy-MM-dd_HH-mm-ss")
                .format(new Date(System.currentTimeMillis()));

        String accName = callDir + "-" + phoneNumber + "-"
                + date + ".raw";//实际是3gp
        File tempFile = new File(recordPath, accName);
        return tempFile;
    }

    //Create file to convert to .wav format
    private File createWavFile()
    {
        File recordPath = new File(Environment.getExternalStorageDirectory()
                , "/mydetectophone");
        if (!recordPath.exists()) {
            recordPath.mkdirs();
            Log.e("recorder", "创建目录");
        }
        else if( !recordPath.isDirectory() && recordPath.canWrite() )
        {
            Log.e("recorder", "删除创建目录");
            recordPath.delete();
            recordPath.mkdirs();
        }
        else
        {
            //you can't access there with write permission.
            //Try other way.
        }

        String callDir = "out";
        if (isCommingNumber)
        {
            callDir = "in";
        }


        date =  new SimpleDateFormat("yy-MM-dd_HH-mm-ss")
                .format(new Date(System.currentTimeMillis()));

        String accName = callDir + "-" + phoneNumber + "-"
                + date + ".wav";
        File wavFile = new File(recordPath, accName);

        return wavFile;
    }



    /*
    *  Convert raw to wav file
    *  @param java.io.File temporay raw file
    *  @param java.io.File destination wav file
    *  @return void
    *
    * */
    private void convertRawToWavFile(File tempFile, File wavFile)
    {
        FileInputStream fin = null;
        FileOutputStream fos = null;
        long audioLength = 0;
        long dataLength = audioLength + 36;
        long sampleRate = RECORD_RATE;
        int channel = 1;
        long byteRate = RECORD_BPP * RECORD_RATE * channel / 8;
        String fileName = null;

        byte[] data = new byte[bufferSize];
        try {
            fin = new FileInputStream(tempFile);
            fos = new FileOutputStream(wavFile);
            audioLength = fin.getChannel().size();
            dataLength = audioLength + 36;
            createWaveFileHeader(fos, audioLength, dataLength, sampleRate, channel, byteRate);

            while (fin.read(data) != -1) {
                fos.write(data);
            }

            uploadFile = wavFile.getAbsoluteFile();
        } catch (FileNotFoundException e) {
            //Log.e("MainActivity:convertRawToWavFile",e.getMessage());
        } catch (IOException e) {
            //Log.e("MainActivity:convertRawToWavFile",e.getMessage());
        } catch (Exception e) {
            //Log.e("MainActivity:convertRawToWavFile",e.getMessage());
        }
    }

    /*
 * To create wav file need to create header for the same
 *
 * @param java.io.FileOutputStream
 * @param long
 * @param long
 * @param long
 * @param int
 * @param long
 * @return void
 */
    private void createWaveFileHeader(FileOutputStream fos, long audioLength, long dataLength, long sampleRate, int channel, long byteRate) {

        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (dataLength & 0xff);
        header[5] = (byte) ((dataLength >> 8) & 0xff);
        header[6] = (byte) ((dataLength >> 16) & 0xff);
        header[7] = (byte) ((dataLength >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channel;
        header[23] = 0;
        header[24] = (byte) (sampleRate & 0xff);
        header[25] = (byte) ((sampleRate >> 8) & 0xff);
        header[26] = (byte) ((sampleRate >> 16) & 0xff);
        header[27] = (byte) ((sampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8); // block align
        header[33] = 0;
        header[34] = 16; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (audioLength & 0xff);
        header[41] = (byte) ((audioLength >> 8) & 0xff);
        header[42] = (byte) ((audioLength >> 16) & 0xff);
        header[43] = (byte) ((audioLength >> 24) & 0xff);

        try {
            fos.write(header, 0, 44);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //Log.e("MainActivity:createWavFileHeader()",e.getMessage());
        }

    }

    /*
    * delete created temperory file
    * @param
    * @return void
    */
    private void deletTempFile() {
        File file = createTempFile();
        file.delete();
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public synchronized boolean isCommingNumber() {
        return isCommingNumber;
    }

    public synchronized void setIsCommingNumber(boolean isCommingNumber) {
        this.isCommingNumber = isCommingNumber;
    }

    private void writeToFile()
    {
        byte bDate[] = new byte[bufferEle];
        FileOutputStream fos =null;
        File recordFile = createTempFile();
        try {
            fos = new FileOutputStream(recordFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (isRecording){
            audioRecorder.read(bDate,0,bufferEle);
        }

        try {
            fos.write(bDate);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}