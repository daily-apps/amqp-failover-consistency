package com.guideapps.checkout.fixture;

import com.guideapps.checkout.domain.model.Payment;
import com.guideapps.checkout.domain.model.PaymentStatus;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;

public class PaymentFixture implements TemplateLoader {

	@Override
	public void load() {
		Fixture.of(Payment.class).addTemplate("new", new Rule(){{
		    add("productId", random(Long.class, range(1L, 1000L)));
		    add("sellerId", random(Long.class, range(1L, 1000L)));
		    add("quantity", random(Integer.class, range(1, 30)));
		    add("status", random(PaymentStatus.class));
		}});
		
		Fixture.of(Payment.class).addTemplate("valid").inherits("new", new Rule(){{
		    add("id", random(Long.class, range(1L, 1000L)));
		}});		
	}
	
}
