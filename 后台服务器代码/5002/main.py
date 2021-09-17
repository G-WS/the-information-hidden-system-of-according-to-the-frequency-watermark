from flask import Flask, render_template, request, jsonify, Response
from werkzeug.utils import secure_filename
from datetime import timedelta
import os
import cv2
from blind_watermark import WaterMark
import datetime
import random

app = Flask(__name__)
UPLOAD_FOLDER = 'static\\images'
app.config['UPLOAD_FOLDER']=UPLOAD_FOLDER

# 输出
@app.route('/')
def hello_world():
    return 'Hello World!'


# 设置允许的文件格式
ALLOWED_EXTENSIONS = set(['png', 'jpg', 'JPG', 'PNG', 'bmp'])


def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS


# 设置静态文件缓存过期时间
app.send_file_max_age_default = timedelta(seconds=1)


class Name:
    def pic_id(self):
        time_now = datetime.datetime.now().strftime("%Y%m%d%H%M%S");
        rann = random.randint(0, 100);
        if rann <= 10:
            rann = str(0) + str(rann);
        pid = str(time_now) + str(rann);
        return pid;


# 添加路由
@app.route('/send', methods=['POST', 'GET'])
def upload():
    if request.method == 'POST':
        # 通过file标签获取文件
        res = {}
        f1 = request.files['file1']
        if not (f1 and allowed_file(f1.filename)):
            res['code']=1
            res['msg'] = "fail"
            return "图片类型错误 应为图片类型：png、PNG、jpg、JPG、bmp"
        # 当前文件所在路径
        basepath = os.path.dirname(__file__)
        # 一定要先创建该文件夹，不然会提示没有该路径
        upload_path1 = os.path.join(basepath, 'static/images', secure_filename(f1.filename))
        # 保存文件
        f1.save(upload_path1)
        # 返回上传成功界面

        f2 = request.files['file2']
        if not (f2 and allowed_file(f2.filename)):
            res['code'] = 1
            res['msg'] = "fail"
            return "图片类型：png、PNG、jpg、JPG、bmp"
        basepath = os.path.dirname(__file__)
        upload_path2 = os.path.join(basepath, 'static/images', secure_filename(f2.filename))
        f2.save(upload_path2)

        bwm1 = WaterMark(password_wm=1, password_img=1)
        # 读取原图
        dirfile0 = upload_path1
        bwm1.read_img(dirfile0)
        # 读取水印
        dirfile = upload_path2
        img1=cv2.imread(dirfile)
        img = cv2.resize(img1,(50,50))
        img = cv2.imread(dirfile)
        size = img.shape
        bwm1.read_wm(dirfile)
        # 打上盲水印
        filen = Name().pic_id() + '.jpg'
        bwm1.embed('result.jpg')  ##加上水印以后的
        a = size[0]
        b = size[1]
        ##filenn = Name().pic_id() + '.jpg'
        print(filen)
        bwm1 = WaterMark(password_wm=1, password_img=1)
        #bwm1.extract(filename=filen, wm_shape=(a, b), out_wm_name='watermark.jpg')
        res['code']=0
        res['msg']="success"
        return render_template('ok.html')
   # 重新返回上传界面
    return render_template('test.html')


@app.route('/extract', methods=['POST', 'GET'])
def extract():
    if request.method == 'POST':
        # 通过file标签获取文件
        f1 = request.files['file1']
        res = {}
        if not (f1 and allowed_file(f1.filename)):
            res['code'] = 1
            res['msg'] = "fail"
            return  "格式错误，请上传图片类型：png、PNG、jpg、JPG、bmp"
        # 当前文件所在路径
        basepath = os.path.dirname(__file__)
        # 一定要先创建该文件夹，不然会提示没有该路径
        upload_path1 = os.path.join(basepath, 'static/images', secure_filename(f1.filename))
        # 保存文件
        f1.save(upload_path1)
        # 返回上传成功界面
        ##filen = Name().pic_id() + '.jpg'
        bwm1 = WaterMark(password_wm=1, password_img=1)
        bwm1.extract(filename=upload_path1, wm_shape=(50,50), out_wm_name='watermark.jpg')

        res['code'] = 0
        res['msg'] = "success"
        return render_template('ok.html')
    # 重新返回上传界面
    return render_template('extract.html')


@app.route("/photo/<imgid>.jpg")
def get_p(imgid):
    with open(r'./{}.jpg'.format(imgid), 'rb') as f:
        image = f.read()
        resp = Response(image, mimetype="image/jpg")
        return resp


if __name__ == '__main__':
    app.run(host='0.0.0.0',port=5002)
