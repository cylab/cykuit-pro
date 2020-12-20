@file:Suppress("EXPERIMENTAL_API_USAGE", "EXPERIMENTAL_UNSIGNED_LITERALS")

import jack.JACK_DEFAULT_MIDI_TYPE
import jack.JackNullOption
import jack.JackPortIsInput
import jack.jack_activate
import jack.jack_client_open
import jack.jack_client_t
import jack.jack_midi_event_get
import jack.jack_midi_event_t
import jack.jack_midi_get_event_count
import jack.jack_nframes_t
import jack.jack_port_get_buffer
import jack.jack_port_register
import jack.jack_port_t
import jack.jack_position_t
import jack.jack_set_process_callback
import jack.jack_transport_query
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.alloc
import kotlinx.cinterop.get
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr
import kotlinx.cinterop.staticCFunction

    private lateinit var client: CPointer<jack_client_t>
    private lateinit var inputPort: CPointer<jack_port_t>
    private var position = nativeHeap.alloc<jack_position_t>()
    private var event = nativeHeap.alloc<jack_midi_event_t>()

    fun main() {
        println(
            """
                           Print JACK MIDI
            This program creates a JACK MIDI port and will
              print in the console all input it recieves
    
                         Press Enter to abort
            """
        )

        try {
            startJackMidiCallback()
            val readLine = readLine()
        }
        catch(e: Exception) {
            println(e.message)
        }
        finally {
            nativeHeap.free(position.rawPtr)
            nativeHeap.free(event.rawPtr)
        }
    }


    fun startJackMidiCallback() {
        client = requireNotNull(jack_client_open("cykuit-pro", JackNullOption, null)) { "Jack server not running?" }
        inputPort = requireNotNull(jack_port_register(client, "midi_in", JACK_DEFAULT_MIDI_TYPE, JackPortIsInput.toULong(), 0))

        val callback = staticCFunction(
            fun(nframes: jack_nframes_t, _: COpaquePointer?): Int {
                return process(nframes)
            }
        )
        jack_set_process_callback(client, callback, null)

        if (jack_activate(client) != 0) {
            throw IllegalStateException("Cannot activate client")
        }
    }

    fun process(nframes: jack_nframes_t): Int {

        val portBuf = jack_port_get_buffer(inputPort, nframes)
        val transport = jack_transport_query(client, position.ptr)

        for (i in 0 until jack_midi_get_event_count(portBuf).toInt()) {
            jack_midi_event_get(event.ptr, portBuf, i.toUInt())
            val frame = position.frame
            val subFrame = event.time
            val size = event.size
            val data0 = event.buffer?.get(0) ?: 0
            val data1 = event.buffer?.get(1) ?: 0
            val data2 = event.buffer?.get(2) ?: 0
            println("Event: $i")
            println(
                "Frame $frame SubFrame#: $subFrame \tMessage ($size):\t" +
                    "${data0}\t${data1}\t${data2}"
            )
        }

        return 0
    }
