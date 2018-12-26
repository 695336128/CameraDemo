package zt.zhang.camerademo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Camera
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.Face
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.ImageReader
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.widget.Button
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    companion object {
        private val ORIENTATIONS: SparseIntArray = SparseIntArray()
    }

    private lateinit var textureView: AutoFitTextureView
    private lateinit var takeButton: Button
    /** 摄像头ID（通常0代表后置摄像头，1代表前置摄像头）*/
    private var mCameraId = "1"
    /** 定义代表摄像头的成员变量 */
    private var cameraDevice: CameraDevice? = null
    /** 预览尺寸*/
    private var previewSize: Size? = null
    private var previewRequestBuilder: CaptureRequest.Builder? = null
    /** 定义用于预览照片的捕获请求*/
    private var previewRequest: CaptureRequest? = null
    /** 定义cameraCaptureSession 成员变量*/
    private var captureSession: CameraCaptureSession? = null
    private var imageReader: ImageReader? = null
    private var mSurfaceTextureListener: TextureView.SurfaceTextureListener? = null
    private var stateCallback: CameraDevice.StateCallback? = null
    private val mCamera: Camera? = null
    private var mFaceDetectSupported = false
    private var mFaceDetectMode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initSetting()
        textureView.surfaceTextureListener = mSurfaceTextureListener
    }

    /**
     * 设置相机的监听和回调
     */
    private fun initSetting() {

        ORIENTATIONS.append(Surface.ROTATION_0, 90)
        ORIENTATIONS.append(Surface.ROTATION_90, 0)
        ORIENTATIONS.append(Surface.ROTATION_180, 270)
        ORIENTATIONS.append(Surface.ROTATION_270, 180)

        mSurfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                return true
            }

            @SuppressLint("NewApi")

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                // 当TextureView可用时，打开摄像头
                setUpCameraOutputs(width, height)
                openCamera(width, height)
            }

        }

        stateCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice?) {
                // 摄像头被打开时激发该方法
                cameraDevice = camera
                createCameraPreviewSession()
            }

            override fun onDisconnected(camera: CameraDevice?) {
                cameraDevice?.close()
                cameraDevice = null
            }

            override fun onError(camera: CameraDevice?, error: Int) {
                cameraDevice?.close()
                cameraDevice = null
                finish()
            }

        }
    }

    /**
     * 初始化布局
     */
    private fun initView() {
        textureView = findViewById(R.id.texture_view)
        takeButton = findViewById(R.id.take_pic_bt)

        takeButton.setOnClickListener { view -> captureStillPicture() }
    }

    /**
     * 捕捉静态图片
     */
    private fun captureStillPicture() {
        try {
            // 创建作为拍照的captureRequest.Builder
            val captureRequestBuilder: CaptureRequest.Builder? = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            captureRequestBuilder?.addTarget(imageReader?.surface)
            // 设置自动对焦模式
            captureRequestBuilder?.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            // 设置自动曝光模式
            captureRequestBuilder?.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
            // 获取设备方向
            val rotation = windowManager.defaultDisplay.rotation
            // 根据设备方向计算设置照片的方向
            captureRequestBuilder?.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation))
            // 停止连续取景
            captureSession?.stopRepeating()
            // 捕获静态图像
            captureSession?.capture(captureRequestBuilder?.build(), object : CameraCaptureSession.CaptureCallback() {
                override fun onCaptureCompleted(session: CameraCaptureSession?, request: CaptureRequest?, result: TotalCaptureResult?) {
                    try {
                        // 重设自动对焦模式
                        previewRequestBuilder?.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL)
                        // 设置自动曝光模式
                        previewRequestBuilder?.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
                        // 打开连续取景模式
                        captureSession?.setRepeatingRequest(previewRequest, null, null)
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }
                }
            }, null)

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    /**
     * 打开摄像头
     */
    private fun openCamera(width: Int, height: Int) {
        val manager: CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            // 打开摄像头
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    // TODO:弹出对话框
                } else {
                    // 申请权限
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 9000)
                }
                return
            } else {
                manager.openCamera(mCameraId, stateCallback, null)
            }

        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    /**
     * 设置摄像头的输出模式
     */
    private fun setUpCameraOutputs(width: Int, height: Int) {
        val manager: CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            // 获取指定摄像头的特性
            val characteristics: CameraCharacteristics = manager.getCameraCharacteristics(mCameraId)
            // 如果是前置摄像头，获取人脸检测参数
            if (mCameraId == "1") {
                // 获取检测人脸参数
                val FD = characteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES)
                val maxFD = characteristics.get(CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT)
                if (FD.isNotEmpty()) {
                    val fdList = ArrayList<Int>()
                    for (FaceD in FD) {
                        fdList.add(FaceD)
                        println("setUpCameraOutputs: FD type:$FaceD")
                    }
                    println("setUpCameraOutputs: FD count: $maxFD")
                    if (maxFD > 0) {
                        mFaceDetectSupported = true
                        mFaceDetectMode = fdList.max()!!
                    }
                }
            }
            // 获取摄像头支持的配置属性
            val map: StreamConfigurationMap = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
            // 获取摄像头支持的最大尺寸
            val largest: Size? = map.getOutputSizes(ImageFormat.JPEG).maxWith(CameraUtils.CompareSizesByArea())
            // 创建一个ImageReader对象，用于获取摄像头的图像数据
            imageReader = ImageReader.newInstance(largest!!.width, largest.height, ImageFormat.JPEG, 2)
            imageReader?.setOnImageAvailableListener({
                //　获取捕获的照片数据
                val image = imageReader!!.acquireNextImage()
                val buffer = image.planes[0].buffer
                val bytes = ByteArray(buffer.remaining())
                // 使用IO流将照片写入指定文件
                val file = File(getExternalFilesDir(null), "pic.jpg")
                buffer.get(bytes)
                try {
                    val output = FileOutputStream(file)
                    output.write(bytes)
                    Toast.makeText(this@MainActivity, "保存：$file", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    image.close()
                }
            }, null)

            // 获取最佳的预览尺寸
            previewSize = CameraUtils.chooseOptimalSize(map.getOutputSizes(SurfaceTexture::class.java), width, height, largest)
            // 根据选中的预览尺寸来调整预览组件的长宽比
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                textureView.setAspectRatio(previewSize!!.width, previewSize!!.height)
            } else {
                textureView.setAspectRatio(previewSize!!.height, previewSize!!.width)
            }
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }


    /**
     * 开始预览
     */
    @SuppressLint("Recycle")
    private fun createCameraPreviewSession() {
        val texture = textureView.surfaceTexture
        texture.setDefaultBufferSize(previewSize!!.width, previewSize!!.height)
        val surface = Surface(texture)
        // 常见作为预览的CaptureRequest.Builder
        previewRequestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        // 将textureView的surface作为CaaptureRequest.Builder的目标
        previewRequestBuilder?.addTarget(surface)
        // 创建CameraCaprureSession，该对象负责管理处理预览请求和拍照请求
        cameraDevice?.createCaptureSession(arrayListOf(surface, imageReader?.surface), object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession?) {
                // 如果摄像头为null,直接结束方法
                if (null == cameraDevice) {
                    return
                }
                // 当摄像头已经准备好时，开始显示预览
                captureSession = session
                try {
                    // 设置自动对焦模式
                    previewRequestBuilder?.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                    // 设置自动曝光模式
                    previewRequestBuilder?.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
                    // 开始显示相机预览
                    previewRequest = previewRequestBuilder?.build()
                    // 设置人脸检测
                    previewRequestBuilder?.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, mFaceDetectMode)
                    // 设置预览时连续捕获图像数据
                    captureSession?.setRepeatingRequest(previewRequest, object : CameraCaptureSession.CaptureCallback() {
                        override fun onCaptureCompleted(session: CameraCaptureSession?, request: CaptureRequest?, result: TotalCaptureResult?) {
                            if (result != null) {
                                process(result)
                            }
                        }

                        override fun onCaptureStarted(session: CameraCaptureSession?, request: CaptureRequest?, timestamp: Long, frameNumber: Long) {
                            super.onCaptureStarted(session, request, timestamp, frameNumber)
                        }

                        override fun onCaptureProgressed(session: CameraCaptureSession?, request: CaptureRequest?, partialResult: CaptureResult?) {
                            if (partialResult != null) {
                                process(partialResult)
                            }
                        }
                    }, null)

                } catch (e: CameraAccessException) {
                    e.printStackTrace()
                }
            }

            override fun onConfigureFailed(session: CameraCaptureSession?) {
                Toast.makeText(this@MainActivity, "配置失败", Toast.LENGTH_SHORT).show()
            }

        }, null)
    }

    /**
     * 对摄像头返回的结果进行处理并获取人脸数据
     */
    private fun process(result: CaptureResult){
        // 获取Face类
        val faces: Array<Face> = result.get(CaptureResult.STATISTICS_FACES)
        // 如果有人脸的话
        if (faces.isNotEmpty()){
            println("face detected" + faces.size)
            // 获取人脸矩形框
            val bounds  = faces[0].bounds
            val y = previewSize?.height!! /2 - bounds.top

            println("height: ${previewSize?.width}")
            println("top: $y")
            println("left: ${bounds.left}")
            println("right: ${bounds.right}")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 9000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "相机权限申请成功", Toast.LENGTH_SHORT).show()
                initSetting()
            }
        }
    }
}
