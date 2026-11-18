package holds

import (
	"context"
	"fmt"
	"time"
)

// Retry llama a fn hasta attempts veces. Entre un intento y el siguiente espera
// base, después 2*base, 4*base... Se rinde antes si ctx vence o se cancela.
func Retry(ctx context.Context, attempts int, base time.Duration, fn func() error) error {
	var err error
	wait := base
	for i := range attempts {
		if err = fn(); err == nil {
			return nil
		}
		if i == attempts-1 {
			break
		}
		select {
		case <-time.After(wait):
			wait *= 2
		case <-ctx.Done():
			return fmt.Errorf("retry: %w (last error: %w)", ctx.Err(), err)
		}
	}
	return fmt.Errorf("retry: %d attempts: %w", attempts, err)
}
