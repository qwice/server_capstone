package com.example.capstone.audio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Script {

    public void startCapture() {
        while (true) {
            try {
                // 키워드를 받으면 오디오 캡쳐: capture.py 실행
                ProcessBuilder captureProcessBuilder = new ProcessBuilder("python3", "/Users/gangjiyeon/Downloads/capstone/kospeech/capture.py");
                captureProcessBuilder.redirectErrorStream(true);
                Process captureProcess = captureProcessBuilder.start();
                System.out.println("capture.py 실행");

                // capture.py의 print부분을 출력
                BufferedReader captureReader = new BufferedReader(new InputStreamReader(captureProcess.getInputStream()));
                String captureOutput;
                while ((captureOutput = captureReader.readLine()) != null) {
                    System.out.println("capture.py: " + captureOutput);
                }

                // capture.py 실행 완료까지 대기
                int captureExitCode = captureProcess.waitFor();
                if (captureExitCode == 0) {
                    System.out.println("capture.py 완료");
                } else {
                    System.out.println("capture.py 실행 실패!!!");
                }

                // ((캡쳐파이 끝나면))음성을 텍스트로 변환: audio.py 실행
                ProcessBuilder audioProcessBuilder = new ProcessBuilder("python3", "/Users/gangjiyeon/Downloads/capstone/kospeech/bin/audio.py");
                audioProcessBuilder.redirectErrorStream(true);
                Process audioProcess = audioProcessBuilder.start();
                System.out.println("audio.py 실행");

                // audio.py의 출력을 터미널에
                BufferedReader audioReader = new BufferedReader(new InputStreamReader(audioProcess.getInputStream()));
                String audioOutput;
                while ((audioOutput = audioReader.readLine()) != null) {
                    System.out.println("audio.py: " + audioOutput);
                }

                // audio.py 실행 완료까지 대기
                int audioExitCode = audioProcess.waitFor();
                if (audioExitCode == 0) {
                    System.out.println("audio.py가 실행 완료");
                } else {
                    System.out.println("audio.py 실행 실패!!!!");
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
