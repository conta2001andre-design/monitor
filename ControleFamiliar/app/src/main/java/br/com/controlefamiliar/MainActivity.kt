package br.com.controlefamiliar

import android.app.Activity
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.security.SecureRandom

class MainActivity : Activity() {
    private lateinit var status: TextView
    private val captureRequest = 4101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun showHome() {
        val root = column()
        root.setPadding(28, 36, 28, 28)
        root.addView(label("Controle Familiar", 28f, true))
        root.addView(label("Protótipo transparente para Android", 16f))
        root.addView(label("Escolha como este aparelho será usado:", 18f, true))
        root.addView(button("Modo Pai — iniciar pareamento") { showParentMode() })
        root.addView(button("Modo Filha — aparelho supervisionado") { showChildMode() })
        root.addView(label("A captura de tela sempre exige confirmação neste aparelho e pode ser encerrada pela pessoa que o está usando.", 14f))
        setContentView(root)
    }

    private fun showParentMode() {
        val root = column()
        root.addView(label("Modo Pai", 26f, true))
        root.addView(label("Demonstração: gere um código temporário para parear os aparelhos. O vínculo remoto ainda não está conectado a um servidor.", 16f))
        root.addView(button("Gerar código de pareamento") {
          val code = SecureRandom().nextInt(900000) + 100000
            status.text = "Código demonstrativo: $code\nDigite-o no aparelho Filha. Este protótipo ainda não valida o código entre aparelhos."
        })
        status = label("Nenhum código gerado.", 18f, true)
        root.addView(status)
        root.addView(button("Voltar") { showHome() })
        setContentView(root)
    }

    private fun showChildMode() {
        val root = column()
        root.addView(label("Modo Filha", 26f, true))
        root.addView(label("A solicitação do Android abrirá uma tela oficial de consentimento. Se aceitar, o sistema indicará o compartilhamento de tela.", 16f))
        root.addView(button("Solicitar autorização de tela") { requestScreenCapture() })
        root.addView(button("Encerrar / voltar") { showHome() })
        status = label("Nenhuma captura está ativa.", 16f, true)
        root.addView(status)
        setContentView(root)
    }

    private fun requestScreenCapture() {
        val manager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        startActivityForResult(manager.createScreenCaptureIntent(), captureRequest)
    }

    @Deprecated("The result is used to report the system's explicit consent in this prototype.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == captureRequest) {
            if (resultCode == RESULT_OK && data != null) {
                status.text = "Autorização concedida pelo Android nesta sessão.\nA transmissão para outro aparelho ainda não foi implementada."
                Toast.makeText(this, "Consentimento recebido; sem transmissão neste protótipo.", Toast.LENGTH_LONG).show()
            } else {
                status.text = "Captura não autorizada. Nenhuma tela foi compartilhada."
            }
        }
    }

    private fun column() = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_VERTICAL
        setPadding(24, 24, 24, 24)
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    private fun label(text: String, size: Float, bold: Boolean = false) = TextView(this).apply {
        this.text = text
        textSize = size
        if (bold) setTypeface(typeface, android.graphics.Typeface.BOLD)
        setPadding(0, 10, 0, 10)
    }

    private fun button(text: String, action: () -> Unit) = Button(this).apply {
        this.text = text
        setOnClickListener { action() }
    }
}
