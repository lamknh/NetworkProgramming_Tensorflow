test_model Docker로 Deployment 하는법
=============

CLI 환경기준으로 작성되었음을 알려드립니다
-------------

1. 도커에 tensorflow serving 이미지를 pull 받는다.
    - docker pull tensorflow/serving
2. docker run -p 8501:8501 \
--mount type=bind,\
source=/Users/yong/NetworkProgramming_Tensorflow/ML_model/test_model,\
target=/models/test_model \      
-e MODEL_NAME=test_model -t tensorflow/serving &
    - 명령어중 source부분은 git clone받은 폴더안에 test_model폴더의 경로를 넣어주면됩니다.
    - ctrl-c ctrl-v시 공백문자도 포함될 수 있으니 이 부분 제거해야합니다. (직접 명령어 치면 제일 확실함)
3. 잘 실행되었다면 보여지는 log에 REST API PORT 8501로 포트가 열렸다는 log를 확인할 수 있습니다.
4. container가 잘 작동하는 상태에서 같은 폴더내에 request_ml_server.py을 실행시키면 Deployment된 모델이 전송받은 테스트 이미지들에 대해 유효한 예측결과값을 잘 return하는지 확인할 수 있습니다.
