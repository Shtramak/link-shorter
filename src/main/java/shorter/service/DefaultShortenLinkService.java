package shorter.service;

import java.util.Optional;

import ioc.PostConstructBean;
import shorter.model.Link;
import shorter.repo.ShortLinksRepo;

import static shorter.model.Link.HTTPLinkTo;

public class DefaultShortenLinkService implements ShortenLinkService {

	private final ShortLinksRepo shortLinksRepo;
	private final ShorterService shorterService;

	@PostConstructBean
	public void init() {
		System.out.println("init() method after DefaultShortenLinkService constructor");
	}

	public DefaultShortenLinkService(ShortLinksRepo repo, ShorterService service) {
		shortLinksRepo = repo;
		shorterService = service;
	}

	@Override
	//@Benchmnark
	public Link shortLink(Link fullLink) {
		String fullPath = fullLink.getPath();
		String shortPath = shorterService.shorten(fullPath);
		shortLinksRepo.put(shortPath, fullPath);
		return HTTPLinkTo(shortPath);
	}

	@Override
	public Optional<Link> fullLink(Link shortLink) {
		String shortPath = shortLink.getPath();
		Optional<String> fullPath = shortLinksRepo.get(shortPath);
		return fullPath.map(Link::HTTPLinkTo);
	}
}
