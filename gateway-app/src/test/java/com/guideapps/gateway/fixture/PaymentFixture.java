package com.guideapps.gateway.fixture;

import com.guideapps.gateway.domain.model.Payment;
import com.guideapps.gateway.domain.model.PaymentStatus;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class PaymentFixture implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(Payment.class).addTemplate("random", new Rule(){{
		    add("productId", random(Long.class, range(1L, 1000L)));
		    add("sellerId", random(Long.class, range(1L, 1000L)));
		    add("quantity", random(Integer.class, range(1, 30)));
		    add("status", random(PaymentStatus.class));
		}});

		Fixture.of(Payment.class).addTemplate("new", new Rule(){{
			add("productId", 1l);
			add("sellerId", 1l);
			add("quantity", 5);
			add("status", PaymentStatus.SUBMITTED);
		}});
		
		Fixture.of(Payment.class).addTemplate("valid").inherits("new", new Rule(){{
		    add("id", 1l);
		}});
		
		Fixture.of(Payment.class).addTemplate("paypal").inherits("valid", new Rule(){{
            add("status", PaymentStatus.APPROVED);
			add("gatewayTransactionToken", "T6t+D1G4chCGzdl4Gg1pyYv0Q60");
		}});
	}
	
}
