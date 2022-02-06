package rsb.internal;

import com.google.inject.Inject;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BooleanSupplier;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Singleton
@Slf4j
public class ClientThread
{
    private final ConcurrentLinkedQueue<BooleanSupplier> invokes = new ConcurrentLinkedQueue<>();

    @Inject
    private Client client;

    /**
     * Immediately invokes a runnable onto the client thread
     * @param r a runnable
     */
    public void invoke(Runnable r)
    {
        invoke(() ->
        {
            r.run();
            return true;
        });
    }

    /**
     * Will run r on the game thread, at a unspecified point in the future.
     * If r returns false, r will be ran again, at a later point
     * @param r the conditional to validate
     *
     */
    public void invoke(BooleanSupplier r)
    {
        if (client.isClientThread())
        {
            if (!r.getAsBoolean())
            {
                invokes.add(r);
            }
            return;
        }

        invokeLater(r);
    }

    /**
     * Will run r on the game thread after this method returns
     * If r returns false, r will be ran again, at a later point
     * @param r the runnable to invoke on the client thread
     */
    public void invokeLater(Runnable r)
    {
        invokeLater(() ->
        {
            r.run();
            return true;
        });
    }

    /**
     * Adds a conditional to validate onto the client thread queue
     * @param r the conditional to add to the client thread queue
     */
    public void invokeLater(BooleanSupplier r)
    {
        invokes.add(r);
    }

    /**
     * Invokes queued actions on the client thread
     */
    void invoke()
    {
        assert client.isClientThread();
        Iterator<BooleanSupplier> ir = invokes.iterator();
        while (ir.hasNext())
        {
            BooleanSupplier r = ir.next();
            boolean remove = true;
            try
            {
                remove = r.getAsBoolean();
            }
            catch (ThreadDeath d)
            {
                throw d;
            }
            catch (Throwable e)
            {
                log.warn("Exception in invoke", e);
            }
            if (remove)
            {
                ir.remove();
            }
            else
            {
                log.trace("Deferring task {}", r);
            }
        }
    }
}